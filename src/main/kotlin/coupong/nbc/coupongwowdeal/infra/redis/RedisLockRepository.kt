package coupong.nbc.coupongwowdeal.infra.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisLockRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    private val valueOps: ValueOperations<String, String> = redisTemplate.opsForValue()

    fun lock(key: String, value: String, timeout: Long): Boolean {
        return valueOps.setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS) == true
    }

    fun unlock(key: String, value: String): Boolean {
        val currentValue = valueOps.get(key)
        return if (currentValue == value) {
            redisTemplate.delete(key)
            true
        } else {
            false
        }
    }
}