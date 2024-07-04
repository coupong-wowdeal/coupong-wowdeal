package coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.couponuser

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.CouponUser
import org.springframework.data.jpa.repository.JpaRepository

interface CouponUserJpaRepository : JpaRepository<CouponUser, Long> {
    fun findByUserId(userId: Long): List<CouponUser>
    fun findByCouponIdAndUserId(couponId: Long, userId: Long): CouponUser?
    fun deleteByCouponId(couponId: Long)
}