package coupong.nbc.coupongwowdeal.domain.common.aop

import coupong.nbc.coupongwowdeal.infra.redis.LockService
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Lock(
    _advice: LockAdvice
) {

    init {
        advice = _advice
    }

    companion object {
        private lateinit var advice: LockAdvice

        fun standard(key: String, holdTime: Long, timeUnit: TimeUnit, function: () -> Unit): Boolean {
            return advice.standard(key, holdTime, timeUnit, function)
        }

        fun <T> spin(key: String, tryTime: Long, timeUnit: TimeUnit, function: () -> T): T? {
            return advice.spin(key, tryTime, timeUnit, function)
        }
    }

    @Component
    class LockAdvice(
        val lockService: LockService
    ) {
        fun standard(key: String, holdTime: Long, timeUnit: TimeUnit, function: () -> Unit): Boolean {
            return lockService.executeWithLock(key, holdTime, timeUnit, function)
        }

        fun <T> spin(key: String, tryTime: Long, timeUnit: TimeUnit, function: () -> T): T? {
            return try {
                lockService.spinUntilLockAcquired(key, tryTime, timeUnit, function)
            } finally {
                lockService.unlock(key)
            }
        }
    }
}