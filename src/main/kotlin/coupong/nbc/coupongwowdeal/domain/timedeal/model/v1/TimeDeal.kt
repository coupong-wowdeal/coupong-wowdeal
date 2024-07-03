package coupong.nbc.coupongwowdeal.domain.timedeal.model.v1

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "time_deal")
class TimeDeal(
    @Column(length = 50)
    var name: String,

    var openedAt: LocalDateTime,

    var closedAt: LocalDateTime,

    // TODO: CRUD 작성시 Coupon과의 관계 설정
    var couponId: Long? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}