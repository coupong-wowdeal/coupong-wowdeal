package coupong.nbc.coupongwowdeal.domain.coupon.service

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponQueryDslRepository
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponRepository
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponRepositoryImpl
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.coupon.CouponJpaRepository
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.couponuser.CouponUserJpaRepository
import coupong.nbc.coupongwowdeal.domain.coupon.service.v1.CouponServiceImpl
import coupong.nbc.coupongwowdeal.domain.user.model.v1.User
import coupong.nbc.coupongwowdeal.domain.user.repository.v1.UserRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import net.jqwik.api.Arbitraries
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CouponServiceDBTest @Autowired constructor(
    private val couponRepository: CouponRepository,
    private val couponUserJpaRepository: CouponUserJpaRepository,
    private val userRepository: UserRepository
) {

    private val couponService = CouponServiceImpl(couponRepository, userRepository)
    private val fixtureMonkey =
        FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @BeforeEach
    fun setUp() {
    }

    @Test
    @Transactional
    fun `1000명의 유저가 500개 재고의 쿠폰에 동시에 발급 시도를 했을 때 발급된 쿠폰은 500개인지 확인`() {
        // given

        val testUserSize = 1000
        val testQuantity = 500
        val nameSet = hashSetOf<String>()
        while (nameSet.size < testUserSize) {
            nameSet.add(Arbitraries.strings().ofMinLength(5).ofMaxLength(10).sample())
        }

        val users = mutableListOf<User>()
        nameSet.forEach {
            users.add(userRepository.saveAndFlush(User(username = it, password = "test")))
        }

        val coupon = couponRepository.save(
            Coupon(
                name = "test",
                expirationAt = LocalDateTime.of(2030, 1, 1, 0, 0),
                totalQuantity = testQuantity,
                currentQuantity = testQuantity,
                discountPrice = 10000
            )
        )

        // when
        users.forEach {
            try {
                couponService.issueCouponToUser(coupon.id!!, it.id!!)
            } catch (e: Exception) {
                println(e.message)
            }
        }

        // then
    }
}

@TestConfiguration
class CouponServiceTestConfig {

    @Bean
    fun couponRepository(
        couponJpaRepository: CouponJpaRepository,
        couponUserJpaRepository: CouponUserJpaRepository,
        couponQueryDslRepository: CouponQueryDslRepository
    ): CouponRepository {
        return CouponRepositoryImpl(couponJpaRepository, couponUserJpaRepository, couponQueryDslRepository)
    }
}