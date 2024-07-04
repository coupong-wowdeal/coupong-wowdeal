package coupong.nbc.coupongwowdeal.domain.coupon.model.v1

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Coupon(
    @Column
    val name: String,

    @Column
    val discountPrice: Int,

    @Column
    val totalQuantity: Int,

    @Column
    var currentQuantity: Int,

    @Column
    var expirationAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {
    fun hasQuantity(): Boolean {
        return currentQuantity > 0
    }

    fun decreaseQuantity() {
        currentQuantity -= 1
    }
}