package coupong.nbc.coupongwowdeal.infra.redis

import coupong.nbc.coupongwowdeal.domain.common.aop.Lock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest
class LockServiceTest(
    lock: Lock
) : BehaviorSpec({

    fun testAction(name: String) {
        Thread.sleep(15)
    }

    given("여러개의 쓰레드가") {
        val executor: ExecutorService = Executors.newFixedThreadPool(3)
        val key = "TEST_LOCK"

        val resultList = mutableListOf<Boolean>()

        val run = Runnable {
            println("thread1 run")

            resultList.add(
                Lock.standard(key, 1000000) {
                    testAction("Thread1")
                }
            )
        }

        val run2 = Runnable {
            println("thread2 run")
            resultList.add(
                Lock.standard(key, 1000000) {
                    testAction("Thread2")
                }
            )
        }

        val run3 = Runnable {
            println("thread3 run")
            resultList.add(
                Lock.standard(key, 1000000) {
                    testAction("Thread3")
                }
            )
        }

        `when`("동시에 executeWithLock()을 실행했을때") {
            repeat(10) {
                executor.execute(run)
                executor.execute(run2)
                executor.execute(run3)
            }

            then("주어진 action은 한번만 실행되어야 한다.") {
                executor.shutdown()
                executor.awaitTermination(1, TimeUnit.MINUTES)

                resultList.filter { it }.size shouldBe 1
            }
        }
    }
})
