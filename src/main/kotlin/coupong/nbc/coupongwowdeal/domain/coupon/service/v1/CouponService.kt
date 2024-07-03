package coupong.nbc.coupongwowdeal.domain.coupon.service.v1

import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponResponse
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponRepository
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository
) {
    fun getCouponList(): List<CouponResponse> {
        TODO("Not yet implement")
    }

    fun useCoupon(userId: Long, couponId: Long) {
        TODO("Not yet implement")
    }

    fun updateCoupon(couponId: Long): CouponResponse {
        TODO("Not yet implement")
    }

    fun expireCoupon(userId: String, couponId: Long) {
        TODO("Not yet implement")
    }
}