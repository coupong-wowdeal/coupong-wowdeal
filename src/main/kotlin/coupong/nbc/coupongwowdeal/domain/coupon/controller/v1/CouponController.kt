package coupong.nbc.coupongwowdeal.domain.coupon.controller.v1

import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponResponse
import coupong.nbc.coupongwowdeal.domain.coupon.service.v1.CouponService
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1")
@RestController
class CouponController(
    private val couponService: CouponService
) {
    @GetMapping("/coupons")
    fun getCouponList(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<List<CouponResponse>> {
        return ResponseEntity.ok(couponService.getCouponList(userPrincipal))
    }

    @PatchMapping("/coupons/{coupon_id}")
    fun useCoupon(
        @PathVariable(name = "coupon_id") couponId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        return ResponseEntity.ok(couponService.useCoupon(couponId, userPrincipal))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/coupons/{coupon_id}")
    fun expireCoupon(
        @PathVariable(name = "coupon_id") couponId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(couponService.expireCoupon(couponId))
    }
}