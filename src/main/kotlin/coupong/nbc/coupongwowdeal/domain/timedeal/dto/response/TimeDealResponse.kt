package coupong.nbc.coupongwowdeal.domain.timedeal.dto.response

import coupong.nbc.coupongwowdeal.domain.timedeal.model.v1.TimeDeal
import java.time.LocalDateTime

data class TimeDealResponse(

    val id: Long?,
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
            val couponId = timeDeal.couponId
            val name = timeDeal.name
            val openedAt = timeDeal.openedAt
            val closedAt = timeDeal.closedAt
            val createdAt = timeDeal.createdAt
            val updatedAt = timeDeal.updatedAt

            return TimeDealResponse(
                id = id,
                couponId = couponId,
                name = name,
                openedAt = openedAt,
                closedAt = closedAt,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
