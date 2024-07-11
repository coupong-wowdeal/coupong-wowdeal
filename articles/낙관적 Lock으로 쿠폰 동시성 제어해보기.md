```kotlin
override fun issueCouponToUser(couponId: Long, userId: Long): CouponResponse {
        var lockResult = false
        var response: CouponResponse? = null
        while (!lockResult) {
            try {
                Transactional {
	                /*  기존 쿠폰 발급 로직*/
                }
            } catch (e: OptimisticLockingFailureException) {
                println("issueCouponToUser OptimisticLockingFailureException error: $e")
                Thread.sleep(500)
            } catch (e: Exception) {
                println("issueCouponToUser error: $e")
                throw e
            }
        }
```

`org.springframework.orm.ObjectOptimisticLockingFailureException: Row was updated or deleted by another transaction`

Lock의 처리 자체는 단순히 `@Version var version: Long? = null` 을 추가해서 해결이 됐다.

데이터를 읽을 때 진짜로 Lock을 거는 게 아니라 데이터의 변경이 일어날 때 버전 정보를 체크해서 다른 트랜잭션이 해당 데이터를 수정했는지 여부를 판단하는 것이 낙관적 락이다. 이후 데이터를 수정하기 전에 버전 정보를 체크해서 다른 트랜잭션에서 해당 데이터를 수정하지 않은 경우에만 수정을 허용한다.

진짜 Lock을 적용하지 않았기에 성능이 빠르지만 동시성 문제를 해결했다고 보기에는 미묘한 감이 있고 데이터의 일관성 측면에서도 문제가 있다. (예를 들어, 1000명의 유저가 500개짜리 쿠폰을 발급 시도했을 때 위 사진에서 version 및 record는 정상적으로 500으로 남았지만 성공 횟수 688, 에러(쿠폰 재고없음) 횟수 312임)

즉 특정 시점에서 여러 트랙잭션이 Quantity를 N → N-1로 바꾸는 것에 성공했다고 추측할 수 있다. 이 때 어떤 유저는 쿠폰 발급을 못받았는데도 성공처리 되었다고 생각해야할지, 동일한 유저가 여러번 시도한 걸로 인식한건지도 확실치 않다.

Transaction이 Commit될 때 에러가 발생하는 거라 예외처리나 Spin의 처리도 코드의 Depth가 길어진다거나 하는 문제가 있고 트랜잭션의 충돌이 발생할 수 밖에 없는 선착순 쿠폰 발급 시나리오에서는 어떻게 작동할지 모르는 낙관적 Lock은 상당히 적합하지 않다고 할 수 있다.

정리: 낙관적 Lock은 테스트 결과로 보아 조회에서 강점을 보이고 수정에서 일관적이지 않은 결과를 제공하므로 선착순 쿠폰 발급 시나리오엔 적합하지 않다. 빠르지만 재시도 로직 구현도 복잡해져 코드 복잡도가 증가하고 트랜잭션이 동시에 접근하는 것을 우선은 허용한 상태라 성공/실패에 대한 여부를 개발자가 확신할 수가 없다.

이런 이유에 따라 접근 자체를 잠궈버리는 비관적 Lock이 수정이 빈번하게 일어나는 시나리오에서 훨씬 적합하다.

자세한 테스트 및 변경은 dev-mysql-lock-optimistic branch에서 확인할 수 있다.