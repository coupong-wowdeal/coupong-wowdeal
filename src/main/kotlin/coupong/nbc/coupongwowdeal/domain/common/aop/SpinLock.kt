package coupong.nbc.coupongwowdeal.domain.common.aop

import coupong.nbc.coupongwowdeal.infra.redis.LockService
import org.springframework.stereotype.Component

@Component
class SpinLock(
    _advice: SpinLockAdvice
) {

    init {
        advice = _advice
    }

    companion object {
        private lateinit var advice: SpinLockAdvice

        operator fun <T> invoke(key: String, timeout: Long, function: () -> T): T? {
            return advice(key, timeout, function)
        }
    }

    @Component
    class SpinLockAdvice(
        val lockService: LockService
    ) {
        operator fun <T> invoke(key: String, timeout: Long, function: () -> T): T? {
            return try {
                lockService.spinUntilLockAcquired(key, timeout, function)
            } finally {
                lockService.unlock(key)
            }
        }
    }
}