package coupong.nbc.coupongwowdeal.domain.coupon.dto

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import java.time.LocalDateTime

data class CouponInfoResponse(
    val id: Long,
    val name: String,
    val discountPrice: Int,
    val totalQuantity: Int,
    var currentQuantity: Int,
    var expirationAt: LocalDateTime,
) {
    companion object {
        fun toResponse(coupon: Coupon): CouponInfoResponse {
            return CouponInfoResponse(
                id = coupon.id!!,
                name = coupon.name,
                discountPrice = coupon.discountPrice,
                totalQuantity = coupon.totalQuantity,
                currentQuantity = coupon.currentQuantity,
                expirationAt = coupon.expirationAt
            )
        }
    }
}
