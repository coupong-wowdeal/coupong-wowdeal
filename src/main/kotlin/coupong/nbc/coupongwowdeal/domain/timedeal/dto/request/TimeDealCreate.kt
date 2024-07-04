package coupong.nbc.coupongwowdeal.domain.timedeal.dto.request

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import coupong.nbc.coupongwowdeal.domain.timedeal.model.v1.TimeDeal
import java.time.LocalDateTime

data class TimeDealCreate(
    val name: String,
    val openedAt: LocalDateTime,
    val closedAt: LocalDateTime,
    val couponName: String,
    val discountPrice: String
) {
    fun toTimeDeal(name: String, openedAt: LocalDateTime, closedAt: LocalDateTime, coupon: Coupon): TimeDeal {
        return TimeDeal(
            name = name,
            openedAt = openedAt,
            closedAt = closedAt,
            coupon = coupon
        )
    }

    fun toCoupon(name: String, discountPrice: String): Coupon {
        return Coupon(
            name = name,
            discountPrice = discountPrice.toInt()
        )
    }
}
