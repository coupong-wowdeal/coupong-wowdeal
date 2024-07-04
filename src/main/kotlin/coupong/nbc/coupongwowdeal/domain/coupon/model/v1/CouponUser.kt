package coupong.nbc.coupongwowdeal.domain.coupon.model.v1

import coupong.nbc.coupongwowdeal.domain.user.model.v1.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class CouponUser(
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    val coupon: Coupon,

    @Column
    var isUsed: Boolean = false,

    @Column
    val issuedAt: LocalDateTime,

    @Column
    var usedAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    fun use() {
        if (this.isUsed) throw IllegalStateException()

        this.isUsed = true
        this.usedAt = LocalDateTime.now()
    }
}