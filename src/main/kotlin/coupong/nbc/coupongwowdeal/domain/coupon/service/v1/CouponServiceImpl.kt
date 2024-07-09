package coupong.nbc.coupongwowdeal.domain.coupon.service.v1

import coupong.nbc.coupongwowdeal.domain.common.aop.Lock
import coupong.nbc.coupongwowdeal.domain.common.aop.Transactional
import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponInfoResponse
import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponResponse
import coupong.nbc.coupongwowdeal.domain.coupon.dto.CreateCouponRequest
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponRepository
import coupong.nbc.coupongwowdeal.domain.user.repository.v1.UserRepository
import coupong.nbc.coupongwowdeal.exception.AccessDeniedException
import coupong.nbc.coupongwowdeal.exception.EmptyQuantityException
import coupong.nbc.coupongwowdeal.exception.ModelNotFoundException
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class CouponServiceImpl(
    private val couponRepository: CouponRepository,
    private val userRepository: UserRepository,
) : CouponService {
    override fun getCouponList(userPrincipal: UserPrincipal): List<CouponResponse> {
        val userId = userPrincipal.id
        return couponRepository.findCouponUserListByUserId(userId)
            .map { CouponResponse.toResponse(it) }
    }

    override fun createCoupon(request: CreateCouponRequest): CouponInfoResponse {
        return request.toCoupon()
            .let { couponRepository.save(it) }
            .let { CouponInfoResponse.toResponse(it) }
    }

    override fun issueCouponToUser(couponId: Long, userId: Long) =
        Lock.spin("LOCK:COUPON:$couponId", 3000, TimeUnit.SECONDS) {
            Transactional {
                check(!couponRepository.isCouponIssued(couponId, userId)) {
                    throw IllegalStateException("User already issue coupon")
                }

                val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
                val coupon =
                    couponRepository.findCouponById(couponId) ?: throw ModelNotFoundException("coupon", couponId)
                check(coupon.hasQuantity()) { throw EmptyQuantityException() }

                couponRepository.issueCouponToUser(coupon, user)
                    .also { coupon.decreaseQuantity() }
                    .let { CouponResponse.toResponse(it) }
            }
        } as CouponResponse

    override fun useCoupon(couponId: Long, userPrincipal: UserPrincipal) {
        Transactional {
            val userId = userPrincipal.id
            val requestUser = (userRepository.findByIdOrNull(userId)
                ?: throw ModelNotFoundException("User", userId))

            couponRepository.findCouponUserByCouponId(couponId, userId)
                ?.also { check(it.user.id == requestUser.id) { throw AccessDeniedException("no permission") } }
                ?.also { check(!it.isExpired()) { throw IllegalStateException("Coupon is expired") } }
                ?.also { it.use() }
                ?: throw ModelNotFoundException("CouponUser", couponId)
        }
    }

    override fun expireCoupon(couponId: Long) = Transactional {
        couponRepository.couponUserDelete(couponId)
        couponRepository.couponDelete(couponId)
    }

    override fun deleteExpiredCoupon() {
        Lock.standard("scheduled_task_lock", 1L, TimeUnit.HOURS) {
            Transactional { couponRepository.deleteExpiredCoupon() }
        }
    }
}