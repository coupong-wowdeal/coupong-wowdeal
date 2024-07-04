package coupong.nbc.coupongwowdeal.domain.coupon.repository.v1

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.CouponUser
import coupong.nbc.coupongwowdeal.domain.user.model.v1.User

interface CouponRepository {
    fun findCouponUserListByUserId(userId: Long): List<CouponUser>
    fun couponUserDelete(couponId: Long)
    fun findCouponUserByCouponId(couponId: Long, userId: Long): CouponUser?
    fun findCouponById(couponId: Long): Coupon?
    fun isCouponIssued(couponId: Long, userId: Long): Boolean
    fun couponDelete(couponId: Long)
    fun issueCouponToUser(coupon: Coupon, user: User): CouponUser
    fun save(coupon: Coupon): Coupon
}