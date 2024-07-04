package coupong.nbc.coupongwowdeal.domain.timedeal.controller.v1

import coupong.nbc.coupongwowdeal.domain.timedeal.dto.request.CreateTimeDealRequest
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.request.UpdateTimeDealRequest
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.response.TimeDealCouponResponse
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.response.TimeDealResponse
import coupong.nbc.coupongwowdeal.domain.timedeal.service.v1.TimeDealServiceImpl
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal
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
    private val timeDealService: TimeDealServiceImpl
) {

    @PostMapping
    fun createTimeDeal(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody timeDealCreate: CreateTimeDealRequest
    ): ResponseEntity<TimeDealResponse> {
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
        @RequestBody timeDealUpdate: UpdateTimeDealRequest
    ): ResponseEntity<TimeDealResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(timeDealService.updateTimeDeal(timeDealId, timeDealUpdate))
    }

    @DeleteMapping("{timeDealId}")
    fun deleteTimeDeal(@PathVariable timeDealId: Long): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(timeDealService.deleteTimeDeal(timeDealId))
    }

    @PostMapping("{timeDealId}")
    fun issueCoupon(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable timeDealId: Long,
    ): ResponseEntity<TimeDealCouponResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(timeDealService.issueCoupon(userPrincipal, timeDealId))
    }
}