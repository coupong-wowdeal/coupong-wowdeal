package coupong.nbc.coupongwowdeal.infra.redis

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer

@Configuration
class EmbeddedRedisConfig(
    @Value("\${spring.data.redis.port}") private val port: Int,
) {
    private var redisServer: RedisServer = RedisServer(port)

    @PostConstruct
    fun startRedis() {
        redisServer.start()
    }

    @PreDestroy
    private fun stopRedis() {
        redisServer.stop()
    }
}
