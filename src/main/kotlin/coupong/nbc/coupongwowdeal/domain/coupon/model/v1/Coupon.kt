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
    val expirationAt: LocalDateTime,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
) {
    private fun validate() {
        require(currentQuantity >= 0) { "Quantity must GOE 0" }
    }

    fun hasQuantity(): Boolean {
        return currentQuantity > 0
    }

    fun decreaseQuantity() {
        currentQuantity -= 1
        validate()
    }
}