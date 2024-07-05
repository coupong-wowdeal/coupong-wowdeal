package coupong.nbc.coupongwowdeal.infra.redis

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest
class LockServiceTest(
    private val redisLockRepository: RedisLockRepository
) : BehaviorSpec({
    val lockService = LockService(redisLockRepository)

    fun testAction(name: String) {
        Thread.sleep(1000)
        println(name)
    }

    given("여러개의 쓰레드가") {
        `when`("동시에 executeWithLock()을 실행했을때") {
            then("주어진 action은 한번만 실행되어야 한다.") {
                val executor: ExecutorService = Executors.newFixedThreadPool(3)
                val key = "TEST_LOCK"

                val resultList = mutableListOf<Boolean>()

                val run = Runnable {
                    println("thread1 run")
                    resultList.add(lockService.executeWithLock(key, 1000000) {
                        testAction("Thread1")
                    })
                }

                val run2 = Runnable {
                    println("thread2 run")
                    resultList.add(lockService.executeWithLock(key, 1000000) {
                        testAction("Thread2")
                    })
                }

                val run3 = Runnable {
                    println("thread3 run")
                    resultList.add(lockService.executeWithLock(key, 1000000) {
                        testAction("Thread2")
                    })
                }

                executor.execute(run)
                executor.execute(run2)
                executor.execute(run3)
                executor.shutdown()
                executor.awaitTermination(1, TimeUnit.MINUTES)

                resultList.filter { it }.size shouldBe 1
            }
        }
    }
})
