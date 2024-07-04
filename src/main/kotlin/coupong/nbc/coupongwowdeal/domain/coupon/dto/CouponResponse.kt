package coupong.nbc.coupongwowdeal.domain.coupon.dto

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.CouponUser
import java.time.LocalDateTime

data class CouponResponse(
    val userId: Long,
    val couponId: Long,
    val couponName: String,
    val discountPrice: Int,
    val issuedAt: LocalDateTime,
    val expirationAt: LocalDateTime
    val usedAt: LocalDateTime?,
) {
    companion object {
        fun toResponse(couponInfo: CouponUser): CouponResponse {
            return CouponResponse(
                userId = couponInfo.user.id!!,
                couponId = couponInfo.id!!,
                couponName = couponInfo.coupon.name,
                discountPrice = couponInfo.coupon.discountPrice,
                issuedAt = couponInfo.issuedAt,
                expirationAt = couponInfo.coupon.expirationAt,
                usedAt = couponInfo.usedAt,
            )
        }
    }
}
