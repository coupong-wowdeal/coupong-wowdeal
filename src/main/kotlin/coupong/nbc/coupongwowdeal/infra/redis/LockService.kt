package coupong.nbc.coupongwowdeal.infra.redis

import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class LockService(
    private val redisLockRepository: RedisLockRepository,
) {
    fun executeWithLock(key: String, timeout: Long, action: () -> Unit): Boolean {
        if (redisLockRepository.lock(key = key, timeout = timeout)) {
            try {
                action()
                return true
            } catch (e: Exception) {
                unlock(key)
                return false
            }
        } else {
            return false
        }
    }

    fun <T> spinUntilLockAcquired(key: String, timeout: Long, action: () -> T): T? {
        var lockResult = false

        var result: T? = null

        while (!lockResult) {
            Thread.sleep(Random.nextLong(15, 30))
            lockResult = if (redisLockRepository.lock(key = key, timeout = timeout)) {
                result = action()
                true
            } else {
                false
            }
        }

        return result
    }

    fun unlock(key: String, value: String = "") {
        redisLockRepository.unlock(key = key, value = value)
    }
}