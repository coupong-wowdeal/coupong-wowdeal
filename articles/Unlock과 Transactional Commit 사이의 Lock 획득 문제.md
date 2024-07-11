```kotlin
@Transactional
override fun issueCouponToUser(couponId: Long, userId: Long): CouponResponse {
    return lockService.executeWithSpinLock("LOCK:COUPON:$couponId", 10000000000) {
        check(!couponRepository.isCouponIssued(couponId, userId)) {
            throw IllegalStateException("User already issue coupon")
            }

            val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)
            val coupon = couponRepository.findCouponById(couponId) ?: throw ModelNotFoundException("coupon", couponId)
            check(coupon.hasQuantity()) { throw IllegalStateException("Coupon has no quantity") }

            couponRepository.issueCouponToUser(coupon, user)
                .also { coupon.decreaseQuantity() }
                .also { couponRepository.save(coupon) }
                .let { CouponResponse.toResponse(it) }
        } as CouponResponse
    }
```

이슈가 발생하던 코드의 모양은 다음과 같다

500개, 1000개 단위의 쓰레드 테스트 진행시 최종 결과로 재고가 2, 3개씩 남거나 쿠폰이 2, 3개 더 발급되는 이슈가 있었는데

이를 처음엔 Lock, Unlock 사이에 요청이 발생할 수 있다고 짐작을 했었고

이를 가지고 튜터님께 튜터링을 요청드린 결과:

### Unlock의 시점이 Transactional의 커밋 시점보다 빨라서 발생한 이슈였다.

재고를 차감하고 Commit하는 시점이 되기 전에 새로운 요청이 Lock을 점유하게 되고 차감되지 않은 재고를 그대로 가져갔기 때문에 애매하게 작은 수의 재고가 남게 되는 이슈였다

이를 방지하기 위해 비즈니스 로직이 담긴 Transcational을 분리해서 Lock 내부에서 호출하게 바꿔 Commit 시점이 Unlock 이전에 이루어지도록 설정하니 이슈가 해결됐다.

부가 이슈: 내부 호출을 방지하면서 서비스 관심사의 분리를 진행하는 방법이 애매하다

현재 임시 처리 방식: CouponLockService를 분리해 해당 Service를 주입받아 비즈니스 로직을 호출해서 내부 호출을 방지하긴 하였으나 복잡도가 늘어난 상태

가능한 처리 방식1: 임시 처리 방식처럼 내부 호출을 방지하며 Transactional을 다른 객체로 분리

가능한 처리 방식1 심화: 내부 호출 방지를 위해 Kotlin Trailing Lambda로 Transactional을 묶어서 동일 객체 내에서 처리

가능한 처리 방식2: TransactionSynchronizationManager.registerSynchronization 을 등록해서 afterCommit을 등록후 LockService에서 Unlock을 분리해 명시적으로 Commit 후에 Unlock이 이루어지도록 수정

```kotlin
TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
    override fun afterCommit() {
        lockService.unlock("LOCK:COUPON:$couponId")
    }
}

@Transactional
fun issue(val couponId: Long) = lock("COUPON:LOCK:!@#@!#@!") {
	registerSynchronization()
}
```
