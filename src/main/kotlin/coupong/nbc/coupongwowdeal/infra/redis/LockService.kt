package coupong.nbc.coupongwowdeal.infra.redis

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.random.Random

@Service
class LockService(
    private val redisLockRepository: RedisLockRepository,
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    fun executeWithLock(key: String, timeout: Long, action: () -> Unit): Boolean {
        val value = UUID.randomUUID().toString()
        return if (redisLockRepository.lock(key, value, timeout)) {
            try {
                action()
                logger.info("complete action()")
            } finally {
                redisLockRepository.unlock(key, value)
                logger.info("complete unlock()")
            }
            true
        } else {
            false
        }
    }

    fun executeWithSpinLock(key: String, timeout: Long, action: () -> Any): Any {
        val value = UUID.randomUUID().toString()
        var lockResult = false

        var result: Any? = null

        while (!lockResult) {
            Thread.sleep(Random.nextLong(1, 30))
            lockResult = if (redisLockRepository.lock(key, value, timeout)) {
                try {
                    result = action()
                } catch (e: Exception) {
                    println("Error occured: ${e.message}")
                    return false
                } finally {
                    redisLockRepository.unlock(key, value)
                }
                true
            } else {
                false
            }
        }
        return result!!
    }
}