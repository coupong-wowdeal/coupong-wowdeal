package coupong.nbc.coupongwowdeal.infra.redis

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import redis.embedded.RedisServer

@Configuration
@EnableRedisRepositories
class RedisConfig(
    @Value("\${spring.data.redis.host}") private val host: String,
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

    @Bean
    fun redissonClient(): RedissonClient {
        val config: Config = Config()
        config.useSingleServer().address = "redis://${host}:${port}"
        return Redisson.create(config)
    }
}
