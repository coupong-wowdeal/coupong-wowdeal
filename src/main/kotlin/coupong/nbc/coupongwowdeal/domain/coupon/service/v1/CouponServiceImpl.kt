package coupong.nbc.coupongwowdeal.domain.coupon.service.v1

import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponInfoResponse
import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponResponse
import coupong.nbc.coupongwowdeal.domain.coupon.dto.CreateCouponRequest
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponRepository
import coupong.nbc.coupongwowdeal.domain.user.repository.v1.UserRepository
import coupong.nbc.coupongwowdeal.exception.AccessDeniedException
import coupong.nbc.coupongwowdeal.exception.ModelNotFoundException
import coupong.nbc.coupongwowdeal.infra.redis.LockService
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponServiceImpl(
    private val couponRepository: CouponRepository,
    private val userRepository: UserRepository,
    private val lockService: LockService
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

    @Transactional
    override fun issueCouponToUser(couponId: Long, userId: Long): CouponResponse {
        check(
            !couponRepository.isCouponIssued(
                couponId,
                userId
            )
        ) { throw IllegalStateException("User already issue coupon") }

        val coupon = couponRepository.findCouponById(couponId) ?: throw ModelNotFoundException("coupon", couponId)
        check(coupon.hasQuantity()) { throw IllegalStateException("Coupon has no quantity") }

        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)

        return couponRepository.issueCouponToUser(coupon, user)
            .also { coupon.decreaseQuantity() }
            .let { CouponResponse.toResponse(it) }
    }

    @Transactional
    override fun useCoupon(couponId: Long, userPrincipal: UserPrincipal) {
        val userId = userPrincipal.id
        val requestUser = (userRepository.findByIdOrNull(userId)
            ?: throw ModelNotFoundException("User", userId))

        couponRepository.findCouponUserByCouponId(couponId, userId)
            ?.also { check(it.user.id == requestUser.id) { throw AccessDeniedException("no permission") } }
            ?.also { check(!it.isExpired()) { throw IllegalStateException("Coupon is expired") } }
            ?.also { it.use() }
            ?: throw ModelNotFoundException("CouponUser", couponId)
    }

    @Transactional
    override fun expireCoupon(couponId: Long) {
        couponRepository.couponUserDelete(couponId)
        couponRepository.couponDelete(couponId)
    }

    @Transactional
    override fun deleteExpiredCoupon() {
        val lockKey = "scheduled_task_lock"
        val lockTimeout = 600000L // 10 minutes

        lockService.executeWithLock(lockKey, lockTimeout) {
            couponRepository.deleteExpiredCoupon()
        }.also { if (!it) println("Could not acquire lock, task is already running") }
    }
}