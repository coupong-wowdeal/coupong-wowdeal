package coupong.nbc.coupongwowdeal.infra.redis

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

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
}