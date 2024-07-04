package coupong.nbc.coupongwowdeal.domain.timedeal.dto.request

import coupong.nbc.coupongwowdeal.domain.coupon.dto.CreateCouponRequest
import coupong.nbc.coupongwowdeal.domain.timedeal.model.v1.TimeDeal
import java.time.LocalDateTime

data class CreateTimeDealRequest(
    val name: String,
    val openedAt: LocalDateTime,
    val closedAt: LocalDateTime,
    val couponName: String,
    val couponExpiredAt: LocalDateTime,
    val couponDiscountPrice: Int,
    val couponTotalQuantity: Int
) {
    fun toTimeDeal(couponId: Long): TimeDeal {
        return TimeDeal(
            name = name,
            openedAt = openedAt,
            closedAt = closedAt,
            couponId = couponId
        )
    }

    fun toCouponCreateRequest(): CreateCouponRequest {
        return CreateCouponRequest(
            name = couponName,
            discountPrice = couponDiscountPrice,
            expirationAt = couponExpiredAt,
            totalQuantity = couponTotalQuantity
        )
    }
}
