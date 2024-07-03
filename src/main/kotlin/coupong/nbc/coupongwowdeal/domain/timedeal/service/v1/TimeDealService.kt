package coupong.nbc.coupongwowdeal.domain.timedeal.service.v1

import coupong.nbc.coupongwowdeal.domain.timedeal.repository.v1.TimeDealRepository
import org.springframework.stereotype.Service

@Service
class TimeDealService(
    private val timeDealRepository: TimeDealRepository
)