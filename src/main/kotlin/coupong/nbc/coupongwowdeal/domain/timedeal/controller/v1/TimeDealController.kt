package coupong.nbc.coupongwowdeal.domain.timedeal.controller.v1

import coupong.nbc.coupongwowdeal.domain.timedeal.service.v1.TimeDealService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/time-deals")
class TimeDealController(
    private val timeDealService: TimeDealService
)