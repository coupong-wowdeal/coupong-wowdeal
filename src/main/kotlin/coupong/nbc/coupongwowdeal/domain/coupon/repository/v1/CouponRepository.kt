package coupong.nbc.coupongwowdeal.domain.coupon.repository.v1

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.CouponUser

interface CouponRepository {
    fun findCouponUserListByUserId(userId: Long): List<CouponUser>
    fun couponUserDelete(couponId: Long)
    fun findCouponUserByCouponId(couponId: Long, userId: Long): CouponUser?
    fun couponDelete(couponId: Long)
    fun deleteExpiredCoupon()
}