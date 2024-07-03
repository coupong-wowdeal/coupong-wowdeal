package coupong.nbc.coupongwowdeal.domain.timedeal.model.v1

import coupong.nbc.coupongwowdeal.domain.common.BaseAuditingEntity
import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "time_deal")
class TimeDeal(
    @Column(length = 50)
    var name: String,

    var openedAt: LocalDateTime,

    var closedAt: LocalDateTime,

    @OneToOne
    var coupon: Coupon
) : BaseAuditingEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}