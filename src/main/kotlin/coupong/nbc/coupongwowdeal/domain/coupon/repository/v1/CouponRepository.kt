package coupong.nbc.coupongwowdeal.domain.coupon.repository.v1

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository : JpaRepository<Coupon, Long>, CouponRepositoryCustom