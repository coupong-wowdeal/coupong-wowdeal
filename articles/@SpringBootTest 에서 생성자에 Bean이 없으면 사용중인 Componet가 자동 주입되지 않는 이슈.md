- 오류 발생

```kotlin
@SpringBootTest
class LockServiceTest(
    
) : BehaviorSpec({
    val lockService = LockService(redisLockRepository)
    
    ...
```

```kotlin
@Component
class Lock(
    _advice: LockAdvice
) {

    init {
        advice = _advice
    }

    companion object {
        private lateinit var advice: LockAdvice //<-- Error 발생

        fun standard(key: String, timeout: Long, function: () -> Unit): Boolean {
            return advice.standard(key, timeout, function)
        }

        fun <T> spin(key: String, timeout: Long, function: () -> T): T? {
            return advice.spin(key, timeout, function)
        }
    }
    ...
```

`kotlin.UninitializedPropertyAccessException: lateinit property advice has not been initialized`

- 정상 작동

```kotlin
@SpringBootTest
class LockServiceTest(
    private val redisLockRepository: RedisLockRepository // 아무상관없는 객체가 있어도 동작
) : BehaviorSpec({
    val lockService = LockService(redisLockRepository)
      
또는 BehaviorSpec을 사용하지 않고 @Test 로 작성시 문제가 없다.

@SpringBootTest
class LockServiceTest {
		@Test
    fun test() {
        /*...*/    
    }
}
```

[https://velog.io/@mash809/코틀린으로-Test-Code-작성하기](https://velog.io/@mash809/%EC%BD%94%ED%8B%80%EB%A6%B0%EC%9C%BC%EB%A1%9C-Test-Code-%EC%9E%91%EC%84%B1%ED%95%98%EA%B8%B0)

동일한 테스트 코드를 BehaviorSpec이 아닌 @Test를 사용시 이러한 문제가 발생하지 않는 것으로 보아 BehaviorSpec 생성자의 Bean과 @Test의 Bean 주입이 차이를 보인 것으로 보인다.

### Spring context가 테스트 인스턴스를 만들고 Bean을 주입하는 과정

Spring Context가 테스트 인스턴스의 생성을 관리할 때 생성자가 존재하면 생성자에 대한 Component 스캔이 이루어지고 적절한 Bean을 찾아 주입하지만 생성자가 비어있을 경우 빈 생성자로 인스턴스가 생성되고 관리된다. 이 과정에서 생성자에 대한 ComponentScan 과정이 발생하기는 하지만 생성자가 비어있어 Bean의 주입이 처리되지 않는다고 추측할 수 있다.

@Test로 작성한 코드는 독립적으로 실행되어 생성자 주입 단계를 넘어서 다시 메소드로 넘어온 단계이기 때문에 다시 Bean 탐색 과정을 거치고 주입한다.

하지만 BehaviorSpec의 경우 생성자로 즉시 주입되는 방식이므로 이를 방지하기 위한 방법으로 `SpringTestLifecycleMode.ROOT` 를 설정해 Spring context가 테스트 전후로 다시 Setup되게 설정하여 방지할 수도 있다.

기본 설정된 `SpringTestLifecycleMode` 는 `PER_METHOD` 인데 이는 각 테스트 메서드가 실행될 때마다 새로운 테스트 인스턴스의 생성자에 관여한다. 생성자의 호출에 따른 Bean의 주입인 건 여전하므로 이 때 주입이 이루어지지 않는 것으로 추측할 수 있다.

`ROOT` 의 경우 인스턴스의 생성과 Bean 주입의 경계가 명확해진다. 생성자에 쓰이지 않았던 의존성 또한 경계가 생겨 독립적으로 처리하기 때문에 생성자와 관계 없던 Bean의 의존성 주입까지 명확하게 처리된다고 추측할 수 있다.

```kotlin
@SpringBootTest
class LockServiceTest(
   
) : BehaviorSpec({

    extensions(SpringTestExtension(SpringTestLifecycleMode.*Root*)) // <- 이거

}
```
