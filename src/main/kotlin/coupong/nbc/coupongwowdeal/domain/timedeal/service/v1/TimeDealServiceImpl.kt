package coupong.nbc.coupongwowdeal.domain.timedeal.service.v1

import coupong.nbc.coupongwowdeal.domain.coupon.service.v1.CouponService
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.request.CreateTimeDealRequest
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.request.UpdateTimeDealRequest
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.response.TimeDealCouponResponse
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.response.TimeDealResponse
import coupong.nbc.coupongwowdeal.domain.timedeal.repository.v1.TimeDealJpaRepository
import coupong.nbc.coupongwowdeal.domain.timedeal.repository.v1.TimeDealRepository
import coupong.nbc.coupongwowdeal.exception.ModelNotFoundException
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TimeDealServiceImpl(
    private val timeDealRepository: TimeDealRepository,
    private val couponService: CouponService
) : TimeDealService {
    @Transactional
    override fun createTimeDeal(userPrincipal: UserPrincipal, request: CreateTimeDealRequest): TimeDealResponse {
        return couponService.createCoupon(request.toCouponCreateRequest())
            .let { request.toTimeDeal(it.id) }
            .let { timeDealRepository.save(it) }
            .let { TimeDealResponse.from(it) }
    }

    override fun getTimeDeals(): List<TimeDealResponse> {
        return timeDealRepository.findAll().map { TimeDealResponse.from(it) }
    }

    @Transactional
    override fun updateTimeDeal(timeDealId: Long, timeDealUpdate: UpdateTimeDealRequest): TimeDealResponse {
        val timeDeal = timeDealRepository.findById(timeDealId) ?: throw ModelNotFoundException("timedeal", timeDealId)
        timeDeal.update(
            name = timeDealUpdate.name,
            openedAt = timeDealUpdate.openedAt,
            closedAt = timeDealUpdate.closedAt
        )
        return TimeDealResponse.from(timeDeal)
    }

    override fun deleteTimeDeal(timeDealId: Long) {
        timeDealRepository.deleteById(timeDealId)
    }

    @Transactional
    override fun issueCoupon(userPrincipal: UserPrincipal, timeDealId: Long): TimeDealCouponResponse {
        val timeDeal =
            timeDealRepository.findById(timeDealId) ?: throw ModelNotFoundException("timedeal", timeDealId)
        check(LocalDateTime.now().isBefore(timeDeal.closedAt)) { throw IllegalStateException("Time deal not opened") }
        return couponService.issueCouponToUser(timeDeal.couponId, userPrincipal.id)
            .let { TimeDealCouponResponse.toResponse(timeDeal, it) }
    }
}