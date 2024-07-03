package coupong.nbc.coupongwowdeal.domain.coupon.controller.v1

import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponResponse
import coupong.nbc.coupongwowdeal.domain.coupon.service.v1.CouponService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1")
@RestController
class CouponController(
    private val couponService: CouponService
) {
    @GetMapping("/users/{user_id}/coupons")
    fun getCouponList(@PathVariable(name = "user_id") userId: Long): ResponseEntity<List<CouponResponse>> {
        return ResponseEntity.ok(couponService.getCouponList())
    }

    @PatchMapping("/users/{user_id}/coupons/{coupon_id}")
    fun useCoupon(
        @PathVariable(name = "user_id") userId: Long,
        @PathVariable(name = "coupon_id") couponId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(couponService.useCoupon(userId, couponId))
    }

    @PutMapping("/coupons/{coupon_id}")
    fun updateCoupon(@PathVariable(name = "coupon_id") couponId: Long): ResponseEntity<CouponResponse> {
        return ResponseEntity.ok(couponService.updateCoupon(couponId))
    }

    @DeleteMapping("/users/{user_id}/coupons/{coupon_id}")
    fun expireCoupon(
        @PathVariable(name = "user_id") userId: String,
        @PathVariable(name = "coupon_id") couponId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(couponService.expireCoupon(userId, couponId))
    }
}