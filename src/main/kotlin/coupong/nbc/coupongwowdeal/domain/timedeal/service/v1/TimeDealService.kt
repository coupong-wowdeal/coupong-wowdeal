package coupong.nbc.coupongwowdeal.domain.timedeal.service.v1

interface TimeDealService {
    fun createTimeDeal(userPrincipal: UserPrincipal, request: CreateTimeDealRequest): TimeDealResponse
    fun getTimeDeals(): List<TimeDealResponse>
    fun updateTimeDeal(timeDealId: Long, timeDealUpdate: UpdateTimeDealRequest): TimeDealResponse
    fun deleteTimeDeal(timeDealId: Long)
    fun issueCoupon(userPrincipal: UserPrincipal, timeDealId: Long): TimeDealCouponResponse
}