package coupong.nbc.coupongwowdeal.domain.timedeal.controller.v1

import coupong.nbc.coupongwowdeal.domain.timedeal.dto.request.TimeDealCreate
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.request.TimeDealUpdate
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.response.TimeDealResponse
import coupong.nbc.coupongwowdeal.domain.timedeal.service.v1.TimeDealService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/time-deals")
class TimeDealController(
    private val timeDealService: TimeDealService
) {

    @PostMapping
    fun createTimeDeal(@RequestBody timeDealCreate: TimeDealCreate): ResponseEntity<TimeDealResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(timeDealService.createTimeDeal(timeDealCreate))
    }

    @GetMapping
    fun getTimeDeals(): ResponseEntity<List<TimeDealResponse>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(timeDealService.getTimeDeals())
    }

    @PutMapping("{timeDealId}")
    fun updateTimeDeal(
        @PathVariable timeDealId: Long,
        @RequestBody timeDealUpdate: TimeDealUpdate
    ): ResponseEntity<TimeDealResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(timeDealService.updateTimeDeal(timeDealId, timeDealUpdate))
    }

    @DeleteMapping("{timeDealId}")
    fun deleteTimeDeal(@PathVariable timeDealId: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(timeDealService.deleteTimeDeal(timeDealId))
    }
}