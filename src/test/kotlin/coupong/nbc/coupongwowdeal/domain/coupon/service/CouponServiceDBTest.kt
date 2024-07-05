package coupong.nbc.coupongwowdeal.domain.coupon.service

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponQueryDslRepository
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponRepository
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.CouponRepositoryImpl
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.coupon.CouponJpaRepository
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.couponuser.CouponUserJpaRepository
import coupong.nbc.coupongwowdeal.domain.coupon.service.v1.CouponService
import coupong.nbc.coupongwowdeal.domain.user.model.v1.User
import coupong.nbc.coupongwowdeal.domain.user.repository.v1.UserRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import net.jqwik.api.Arbitraries
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CouponServiceDBTest @Autowired constructor(
    private val couponService: CouponService,
    private val couponUserJpaRepository: CouponUserJpaRepository,
    private val couponJpaRepository: CouponJpaRepository,
    private val userRepository: UserRepository
) {

    @Test
    fun `1000명의 유저가 500개 재고의 쿠폰에 동시에 발급 시도를 했을 때 발급된 쿠폰은 500개인지 확인`() {
        runBlocking {
            // given
            val testUserSize = 1000
            val testQuantity = 500
            var successCount = 0

            val nameSet = hashSetOf<String>()
            while (nameSet.size < testUserSize) {
                nameSet.add(Arbitraries.strings().ofMinLength(5).ofMaxLength(10).sample())
            }

            nameSet.forEach {
                userRepository.saveAndFlush(User(username = it, password = "test"))
            }

            couponJpaRepository.saveAndFlush(
                Coupon(
                    name = "test",
                    expirationAt = LocalDateTime.of(2030, 1, 1, 0, 0),
                    totalQuantity = testQuantity,
                    currentQuantity = testQuantity,
                    discountPrice = 10000
                )
            )

            // when
            val jobs = List(testUserSize) { index ->
                CoroutineScope(Dispatchers.IO).async {
                    try {
                        couponService.issueCouponToUser(1L, index.toLong() + 1)
                        successCount++
                    } catch (e: Exception) {
                        println("Error happens ${e.message}")
                    }
                }
            }

            jobs.joinAll()

            // then
            val findAllCouponUserSize = couponUserJpaRepository.findAll().size
            val finalCurrentQuantitiy = couponJpaRepository.findByIdOrNull(1L)?.currentQuantity
            println("successCount: $successCount")
            println("couponUserJpaRepository.findAll().size: $findAllCouponUserSize")
            println("couponJpaRepository.findByIdOrNull(1L)?.currentQuantity: $finalCurrentQuantitiy")

            successCount shouldBe testQuantity
            findAllCouponUserSize shouldBe testQuantity
            finalCurrentQuantitiy shouldBe 0
        }
    }

    @Test
    fun `500명의 유저가 500개 재고의 쿠폰에 동시에 발급 시도를 했을 때 발급된 쿠폰은 500개인지 확인`() {
        runBlocking {
            // given
            val testUserSize = 500
            val testQuantity = 500
            var successCount = 0

            val nameSet = hashSetOf<String>()
            while (nameSet.size < testUserSize) {
                nameSet.add(Arbitraries.strings().ofMinLength(5).ofMaxLength(10).sample())
            }

            nameSet.forEach {
                userRepository.saveAndFlush(User(username = it, password = "test"))
            }

            couponJpaRepository.saveAndFlush(
                Coupon(
                    name = "test",
                    expirationAt = LocalDateTime.of(2030, 1, 1, 0, 0),
                    totalQuantity = testQuantity,
                    currentQuantity = testQuantity,
                    discountPrice = 10000
                )
            )

            // when
            val jobs = List(testUserSize) { index ->
                CoroutineScope(Dispatchers.IO).async {
                    try {
                        couponService.issueCouponToUser(1L, index.toLong() + 1)
                        successCount++
                    } catch (e: Exception) {
                        println("Error happens ${e.message}")
                    }
                }
            }

            jobs.joinAll()

            // then
            val findAllCouponUserSize = couponUserJpaRepository.findAll().size
            val finalCurrentQuantitiy = couponJpaRepository.findByIdOrNull(1L)?.currentQuantity
            println("successCount: $successCount")
            println("couponUserJpaRepository.findAll().size: $findAllCouponUserSize")
            println("couponJpaRepository.findByIdOrNull(1L)?.currentQuantity: $finalCurrentQuantitiy")

            successCount shouldBe testQuantity
            findAllCouponUserSize shouldBe testQuantity
            finalCurrentQuantitiy shouldBe 0
        }
    }

    @Test
    fun `200명의 유저가 500개 재고의 쿠폰에 동시에 발급 시도를 했을 때 발급된 쿠폰은 200개인지 확인`() {
        runBlocking {
            // given
            val testUserSize = 200
            val testQuantity = 500
            var successCount = 0

            val nameSet = hashSetOf<String>()
            while (nameSet.size < testUserSize) {
                nameSet.add(Arbitraries.strings().ofMinLength(5).ofMaxLength(10).sample())
            }

            nameSet.forEach {
                userRepository.saveAndFlush(User(username = it, password = "test"))
            }

            couponJpaRepository.saveAndFlush(
                Coupon(
                    name = "test",
                    expirationAt = LocalDateTime.of(2030, 1, 1, 0, 0),
                    totalQuantity = testQuantity,
                    currentQuantity = testQuantity,
                    discountPrice = 10000
                )
            )

            // when
            val jobs = List(testUserSize) { index ->
                CoroutineScope(Dispatchers.IO).async {
                    try {
                        couponService.issueCouponToUser(1L, index.toLong() + 1)
                        successCount++
                    } catch (e: Exception) {
                        println("Error happens ${e.message}")
                    }
                }
            }

            jobs.joinAll()

            // then
            val findAllCouponUserSize = couponUserJpaRepository.findAll().size
            val finalCurrentQuantitiy = couponJpaRepository.findByIdOrNull(1L)?.currentQuantity
            println("successCount: $successCount")
            println("couponUserJpaRepository.findAll().size: $findAllCouponUserSize")
            println("couponJpaRepository.findByIdOrNull(1L)?.currentQuantity: $finalCurrentQuantitiy")

            successCount shouldBe testUserSize
            findAllCouponUserSize shouldBe testUserSize
            finalCurrentQuantitiy shouldBe testQuantity - testUserSize
        }
    }

    @AfterEach
    fun tearDown() {
        couponUserJpaRepository.deleteAll()
        couponJpaRepository.deleteAll()
        userRepository.deleteAll()
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