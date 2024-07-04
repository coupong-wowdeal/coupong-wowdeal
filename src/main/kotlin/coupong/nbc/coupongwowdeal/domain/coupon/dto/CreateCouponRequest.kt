package coupong.nbc.coupongwowdeal.domain.coupon.dto

import java.time.LocalDateTime

data class CreateCouponRequest(
    val name: String,
    val expirationAt: LocalDateTime,
    val discountPrice: Int,
    val totalQuantity: Int,
)
