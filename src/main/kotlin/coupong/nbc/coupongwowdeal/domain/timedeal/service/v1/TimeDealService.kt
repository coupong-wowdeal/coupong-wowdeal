package coupong.nbc.coupongwowdeal.domain.timedeal.service.v1

import coupong.nbc.coupongwowdeal.domain.coupon.service.v1.CouponService
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.request.UpdateTimeDealRequest
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.response.TimeDealResponse
import coupong.nbc.coupongwowdeal.domain.timedeal.repository.v1.TimeDealRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TimeDealService(
    private val timeDealRepository: TimeDealRepository,
    private val couponService: CouponService
) {
    @Transactional
    fun createTimeDeal(request: CreateTimeDealRequest): TimeDealResponse? {
        val coupon =
            couponService.createCoupon(timeDealCreateRequest.toCouponCreateRequest())
        val timeDeal = timeDealCreateRequest.toTimeDeal(coupon)
        timeDealRepository.save(timeDeal)
        return TimeDealResponse.from(timeDeal)
    }

    fun getTimeDeals(): List<TimeDealResponse>? {
        return timeDealRepository.findAll().map { TimeDealResponse.from(it) }
    }

    fun updateTimeDeal(timeDealId: Long, timeDealUpdate: UpdateTimeDealRequest): TimeDealResponse? {
        val timeDeal = timeDealRepository.findById(timeDealId).orElseThrow()
        timeDeal.update(timeDealUpdate.name, timeDealUpdate.openedAt, timeDealUpdate.closedAt)
        return TimeDealResponse.from(timeDeal)
    }

    fun deleteTimeDeal(timeDealId: Long) {
        timeDealRepository.deleteById(timeDealId)
    }
}