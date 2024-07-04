package coupong.nbc.coupongwowdeal.domain.coupon.service.v1

import coupong.nbc.coupongwowdeal.domain.coupon.dto.CouponResponse
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponRepository
import coupong.nbc.coupongwowdeal.domain.user.repository.v1.UserRepository
import coupong.nbc.coupongwowdeal.exception.AccessDeniedException
import coupong.nbc.coupongwowdeal.exception.ModelNotFoundException
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponServiceImpl(
    private val couponRepository: CouponRepository,
    private val userRepository: UserRepository,
) : CouponService {
    override fun getCouponList(userPrincipal: UserPrincipal): List<CouponResponse> {
        val userId = userPrincipal.id
        return couponRepository.findCouponUserListByUserId(userId)
            .map { CouponResponse.toResponse(it) }
    }

    override fun createCoupon() {
        TODO("Coupon 생성")
        TODO("CouponQuantity 생성")
    }

    override fun issueCoupon(): CouponResponse {
        TODO("발급 이력 확인")
        TODO("쿠폰 재고 확인")
        TODO("유저에게 쿠폰을 발급한다.")
    }

    @Transactional
    override fun useCoupon(couponId: Long, userPrincipal: UserPrincipal) {
        val userId = userPrincipal.id
        val requestUser = (userRepository.findByIdOrNull(userId)
            ?: throw ModelNotFoundException("User", userId))

        //TODO 만료시간 체크 로직 필요
        couponRepository.findCouponUserByCouponId(couponId, userId)
            ?.also { check(it.user.id == requestUser.id) { throw AccessDeniedException("no permission") } }
            ?.also { it.use() }
            ?: throw ModelNotFoundException("CouponUser", couponId)
    }

    @Transactional
    override fun expireCoupon(couponId: Long) {
        couponRepository.couponUserDelete(couponId)
        couponRepository.couponDelete(couponId)
    }

    @Transactional
    override fun deleteExpiredCoupon() {
        couponRepository.deleteExpiredCoupon()
    }
}