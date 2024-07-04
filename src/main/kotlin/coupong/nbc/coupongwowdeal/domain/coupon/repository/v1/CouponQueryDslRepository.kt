package coupong.nbc.coupongwowdeal.domain.coupon.repository.v1

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPQLQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.QCoupon
import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.QCouponUser
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class CouponQueryDslRepository(
    private val queryFactory: JPAQueryFactory
) {
    private val coupon = QCoupon.coupon
    private val couponUser = QCouponUser.couponUser

    fun deleteExpiredCoupon() {
        val expiredCouponIds = JPAExpressions
            .select(coupon.id)
            .from(coupon)
            .where(coupon.expirationAt.before(LocalDateTime.now()))

        queryFactory.delete(couponUser)
            .where(deleteSearch(expiredCouponIds))
            .execute()

        queryFactory.delete(coupon)
            .where(coupon.expirationAt.before(LocalDateTime.now()))
            .execute()
    }

    private fun deleteSearch(ids: JPQLQuery<Long>): BooleanBuilder =
        BooleanBuilder().and(couponUser.coupon.id.`in`(ids))
}
