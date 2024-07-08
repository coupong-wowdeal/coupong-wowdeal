package coupong.nbc.coupongwowdeal.domain.coupon.service.v1

import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponResponse
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponRepository
import coupong.nbc.coupongwowdeal.domain.user.repository.v1.UserRepository
import coupong.nbc.coupongwowdeal.exception.ModelNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class CouponLockService(
    private val couponRepository: CouponRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun issueCoupon(couponId: Long, userId: Long): CouponResponse {
        check(!couponRepository.isCouponIssued(couponId, userId)) {
            throw IllegalStateException("User already issue coupon")
        }

        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
        val coupon = couponRepository.findCouponById(couponId) ?: throw ModelNotFoundException("coupon", couponId)
        check(coupon.hasQuantity()) { throw IllegalStateException("Coupon has no quantity") }

        return couponRepository.issueCouponToUser(coupon, user)
            .also { coupon.decreaseQuantity() }
            .also { couponRepository.save(coupon) }
            .let { CouponResponse.toResponse(it) }
    }
}