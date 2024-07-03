package coupong.nbc.coupongwowdeal.domain.coupon.repository.v1

import coupong.nbc.coupongwowdeal.infra.querydsl.QueryDslConfig
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(
    private val queryDslConfig: QueryDslConfig
) : CouponRepositoryCustom