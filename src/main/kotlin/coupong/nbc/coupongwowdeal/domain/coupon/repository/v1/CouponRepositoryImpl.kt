package coupong.nbc.coupongwowdeal.domain.coupon.repository.v1

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.CouponUser
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.coupon.CouponJpaRepository
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.couponuser.CouponUserJpaRepository
import org.springframework.stereotype.Repository

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
        return couponUserJpaRepository.deleteByCouponId(couponId)
    }

    override fun couponDelete(couponId: Long) {
        return couponJpaRepository.deleteById(couponId)
    }
}