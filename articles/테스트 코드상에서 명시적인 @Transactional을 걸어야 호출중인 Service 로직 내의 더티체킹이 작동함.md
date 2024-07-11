```kotlin
@Transactional
    override fun issueCouponToUser(couponId: Long, userId: Long): CouponResponse {
        check(
            !couponRepository.isCouponIssued(
                couponId,
                userId
            )
        ) { throw IllegalStateException("User already issue coupon") }

        val coupon = couponRepository.findCouponById(couponId) ?: throw ModelNotFoundException("coupon", couponId)
        check(coupon.hasQuantity()) { throw IllegalStateException("Coupon has no quantity") }

        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)

        return couponRepository.issueCouponToUser(coupon, user)
            .also { coupon.decreaseQuantity() }
            .let { CouponResponse.toResponse(it) }
    }
```

테스트 대상인 쿠폰 발급 메소드 `issueCouponToUser`
`decreaseQuantity` 를 진행시 dirty checking으로 1씩 깎인다
단순히 API 호출로 테스트할 때는 정상 작동하나 테스트 코드에서 호출했을 시에는 차감되지 않는 문제가 있었다.

```kotlin
val couponService = CouponServiceImpl(/*repository*/)

@Test
    @Transactional
    fun `1000명의 유저가 500개 재고의 쿠폰에 동시에 발급 시도를 했을 때 발급된 쿠폰은 500개인지 확인`() {
        // given

        val testUserSize = 1000
        val testQuantity = 500
        val nameSet = hashSetOf<String>()
        while (nameSet.size < testUserSize) {
            nameSet.add(Arbitraries.strings().ofMinLength(5).ofMaxLength(10).sample())
        }

        val users = mutableListOf<User>()
        nameSet.forEach {
            users.add(userRepository.saveAndFlush(User(username = it, password = "test")))
        }

        val coupon = couponRepository.save(
            Coupon(
                name = "test",
                expirationAt = LocalDateTime.of(2030, 1, 1, 0, 0),
                totalQuantity = testQuantity,
                currentQuantity = testQuantity,
                discountPrice = 10000
            )
        )

        // when
        users.forEach {
            try {
                couponService.issueCouponToUser(coupon.id!!, it.id!!)
            } catch (e: Exception) {
                println(e.message)
            }
        }

        // then
    }
```

문제를 확인했던 테스트 코드다 (@Transactional 이 추가되면서 해결됨)

그런데 테스트 코드는 DB 테스트 이후 롤백을 위해 @Transactional 이 기본적으로 걸리는 것으로 알고 있었고
전파 수준도 단순히 REQUIRED를 사용한다고 한다.

그래서 조사해보니 내가 알던 내용은 `@DataJpaTest` 에 해당하는 내용으로 @SpringBootTest 는 Transactional을 걸지 않고 각 메소드별로 Transactional의 경계를 설정하길 기대하고 있다.
결과적으로 @Transactional에 대한 의문은 풀렸지만 이미 Transactional이 걸려있는 issueCouponToUser 에서 왜 Dirty checking이 동작하지 않는가는 아직 아리송하다.
예상해보기론 Transactional의 경계가 문제가 되는 것 같은데 Service 로직이 끝날 때마다 Flush 처리되어야 하는 게 아닌가?

이는 호출자가 영속성 컨텍스트의 관리를 받고 있지 않거나 
영속성 컨텍스트에 들어간 내용이 커밋되는 시점이 없는 것 둘 중 하나로 결론이 났다. ( flush의 Commit 시점이 존재하지 않음 )

## 7월 5일 최종 해결

@Transactional은 Spring AOP 방식으로 동작하는데 Spring Container가 관리하는 Bean에서만 동작한다. 정확히는 Bean의 프록시만 생성이 가능한 구조로 이루어져 있다.

그렇기에 테스트 코드에서 직접 생성자 주입 방식으로 생성한 ServiceImpl() 은 Bean으로 등록되지 않아 Spring container의 관리를 받지 않고 있고 Spring AOP 방식으로 작동하는 @Transactional 은 제대로 동작하지 않았으니 영속성 컨텍스트가 제대로 열리지 않아 1차 캐시의 도움을 받지 못해 Dirty checking이 일어나지 않은 것이다.

그래서 @SpringBootTest에서 모든 Transactional을 재차 제거하고 Service를 Bean으로 만들어주니 해결됐다. (모든 Transactional을 제거한 이유는 비동기 테스트중에 Rollback이 일어나 비동기 메소드에서 불러오는 Entity가 Rollback 처리 되기 때문이다.)
