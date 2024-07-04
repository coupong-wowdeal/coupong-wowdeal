package coupong.nbc.coupongwowdeal.domain.timedeal.dto.response

import coupong.nbc.coupongwowdeal.domain.timedeal.model.v1.TimeDeal
import java.time.LocalDateTime

data class TimeDealResponse(

    val id: Long,
    val couponId: Long,
    val name: String,
    val openedAt: LocalDateTime,
    val closedAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(timeDeal: TimeDeal): TimeDealResponse {
            val id = timeDeal.id
            val couponId = timeDeal.coupon.id
            val name = timeDeal.name
            val openedAt = timeDeal.openedAt
            val closedAt = timeDeal.closedAt
            val createdAt = timeDeal.createdAt
            val discountPrice = timeDeal.coupon.discountPrice

            return TimeDealResponse(
                name = name,
                openedAt = openedAt,
                closedAt = closedAt,
                couponName = couponName,
                discountPrice = discountPrice.toString()
            )
        }
    }
}
