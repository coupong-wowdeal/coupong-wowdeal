package coupong.nbc.coupongwowdeal.domain.coupon.service.v1

import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponResponse
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal

interface CouponService {

    fun getCouponList(userPrincipal: UserPrincipal): List<CouponResponse>
    fun createCoupon()
    fun issueCoupon(): CouponResponse
    fun useCoupon(couponId: Long, userPrincipal: UserPrincipal)
    fun expireCoupon(couponId: Long)
}
