package coupong.nbc.coupongwowdeal.domain.coupon.repository.v1

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.CouponUser
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.coupon.CouponJpaRepository
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.couponuser.CouponUserJpaRepository
import coupong.nbc.coupongwowdeal.domain.user.model.v1.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class CouponRepositoryImpl(
    private val couponJpaRepository: CouponJpaRepository,
    private val couponUserJpaRepository: CouponUserJpaRepository,
    private val couponQueryDslRepository: CouponQueryDslRepository
) : CouponRepository {
    override fun findCouponUserByCouponId(couponId: Long, userId: Long): CouponUser? {
        return couponUserJpaRepository.findByCouponIdAndUserId(couponId, userId)
    }

    override fun findCouponUserListByUserId(userId: Long): List<CouponUser> {
        return couponUserJpaRepository.findByUserId(userId)
    }

    override fun couponUserDelete(couponId: Long) {
        couponUserJpaRepository.deleteByCouponId(couponId)
    }

    override fun isCouponIssued(couponId: Long, userId: Long): Boolean {
        return couponUserJpaRepository.existsByCouponIdAndUserId(couponId, userId)
    }

    override fun couponDelete(couponId: Long) {
        couponJpaRepository.deleteById(couponId)
    }

    override fun deleteExpiredCoupon() {
        couponQueryDslRepository.deleteExpiredCoupon()
    }

    override fun issueCouponToUser(coupon: Coupon, user: User): CouponUser {
        return couponUserJpaRepository.save(
                    CouponUser(
                        coupon = coupon,
                        user = user,
                        issuedAt = LocalDateTime.now()
                    )
                )
    }

    override fun findCouponById(couponId: Long): Coupon? {
        return couponJpaRepository.findByIdOrNull(couponId)
    }

    override fun save(coupon: Coupon): Coupon {
        return couponJpaRepository.save(coupon)
    }
}