package coupong.nbc.coupongwowdeal.infra.redis

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class LockService(
    private val redissonClient: RedissonClient,
) {
    fun executeWithLock(key: String, holdTime: Long, timeUnit: TimeUnit, action: () -> Unit): Boolean {
        val lock = redissonClient.getLock(key)
        if (lock.isLocked) return false
        try {
            lock.lock(holdTime, timeUnit)
            action()
        } catch (e: Exception) {
            lock.unlock()
            return false
        }
        return true
    }

    fun <T> spinUntilLockAcquired(key: String, tryTime: Long, timeUnit: TimeUnit, action: () -> T): T? {
        var lockResult = false

        var result: T? = null

        val rLock = redissonClient.getLock(key)
        if (rLock.tryLock(tryTime, timeUnit)) {
            result = action()
        }
        return result
    }

    fun unlock(key: String) {
        val rLock = redissonClient.getLock(key)
        rLock.unlock()
    }
}