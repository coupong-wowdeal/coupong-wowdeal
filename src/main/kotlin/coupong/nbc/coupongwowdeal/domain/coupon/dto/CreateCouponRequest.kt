package coupong.nbc.coupongwowdeal.domain.coupon.dto

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import java.time.LocalDateTime

data class CreateCouponRequest(
    val name: String,
    val expirationAt: LocalDateTime,
    val discountPrice: Int,
    val totalQuantity: Int,
) {
    fun toCoupon(): Coupon {
        return Coupon(
            name = name,
            discountPrice = discountPrice,
            totalQuantity = totalQuantity,
            currentQuantity = totalQuantity,
            expirationAt = expirationAt
        )
    }
}
