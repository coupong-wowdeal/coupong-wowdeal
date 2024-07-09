package coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.coupon

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import java.util.Optional

interface CouponJpaRepository : JpaRepository<Coupon, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    override fun findById(id: Long): Optional<Coupon>
}