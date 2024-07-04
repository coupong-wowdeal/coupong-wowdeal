package coupong.nbc.coupongwowdeal.domain.timedeal.dto.request

import java.time.LocalDateTime

data class TimeDealUpdate(
    val name: String,
    val openedAt: LocalDateTime,
    val closedAt: LocalDateTime,
)
