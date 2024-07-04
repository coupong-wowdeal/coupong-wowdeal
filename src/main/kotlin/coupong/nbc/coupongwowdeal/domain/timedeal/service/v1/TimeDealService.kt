package coupong.nbc.coupongwowdeal.domain.timedeal.service.v1

import coupong.nbc.coupongwowdeal.domain.timedeal.dto.request.CreateTimeDealRequest
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.request.UpdateTimeDealRequest
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.response.TimeDealCouponResponse
import coupong.nbc.coupongwowdeal.domain.timedeal.dto.response.TimeDealResponse
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal

interface TimeDealService {
    fun createTimeDeal(userPrincipal: UserPrincipal, request: CreateTimeDealRequest): TimeDealResponse
    fun getTimeDeals(): List<TimeDealResponse>
    fun updateTimeDeal(timeDealId: Long, timeDealUpdate: UpdateTimeDealRequest): TimeDealResponse
    fun deleteTimeDeal(timeDealId: Long)
    fun issueCoupon(userPrincipal: UserPrincipal, timeDealId: Long): TimeDealCouponResponse
}