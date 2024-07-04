package coupong.nbc.coupongwowdeal.domain.timedeal.dto.response

import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponResponse
import coupong.nbc.coupongwowdeal.domain.timedeal.model.v1.TimeDeal

data class TimeDealCouponResponse(
    val timeDealId: Long,
    val timeDealName: String,
    val couponResponse: CouponResponse
) {
    companion object {
        fun toResponse(timeDeal: TimeDeal, couponResponse: CouponResponse): TimeDealCouponResponse {
            return TimeDealCouponResponse(
                timeDealId = timeDeal.id!!,
                timeDealName = timeDeal.name,
                couponResponse = couponResponse
            )
        }
    }
}
