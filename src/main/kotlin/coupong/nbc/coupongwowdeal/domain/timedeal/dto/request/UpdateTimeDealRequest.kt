package coupong.nbc.coupongwowdeal.domain.timedeal.dto.request

import java.time.LocalDateTime

data class UpdateTimeDealRequest(
    val name: String,
    val openedAt: LocalDateTime,
    val closedAt: LocalDateTime,
)
