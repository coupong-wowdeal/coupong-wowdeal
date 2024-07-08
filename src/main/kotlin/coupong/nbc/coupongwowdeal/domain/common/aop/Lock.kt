package coupong.nbc.coupongwowdeal.domain.common.aop

import coupong.nbc.coupongwowdeal.infra.redis.LockService
import org.springframework.stereotype.Component

@Component
class Lock(
    _advice: LockAdvice
) {

    init {
        advice = _advice
    }

    companion object {
        private lateinit var advice: LockAdvice

        fun standard(key: String, timeout: Long, function: () -> Unit): Boolean {
            return advice.standard(key, timeout, function)
        }

        fun <T> spin(key: String, timeout: Long, function: () -> T): T? {
            return advice.spin(key, timeout, function)
        }
    }

    @Component
    class LockAdvice(
        val lockService: LockService
    ) {
        fun standard(key: String, timeout: Long, function: () -> Unit): Boolean {
            return lockService.executeWithLock(key, timeout, function)
        }

        fun <T> spin(key: String, timeout: Long, function: () -> T): T? {
            return try {
                lockService.spinUntilLockAcquired(key, timeout, function)
            } finally {
                lockService.unlock(key)
            }
        }
    }
}