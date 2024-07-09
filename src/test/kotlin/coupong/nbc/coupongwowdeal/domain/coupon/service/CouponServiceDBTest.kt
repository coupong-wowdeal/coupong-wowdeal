package coupong.nbc.coupongwowdeal.domain.coupon.service

import coupong.nbc.coupongwowdeal.domain.coupon.model.v1.Coupon
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.coupon.CouponJpaRepository
import coupong.nbc.coupongwowdeal.domain.coupon.repository.v1.couponuser.CouponUserJpaRepository
import coupong.nbc.coupongwowdeal.domain.coupon.service.v1.CouponService
import coupong.nbc.coupongwowdeal.domain.user.model.v1.User
import coupong.nbc.coupongwowdeal.domain.user.repository.v1.UserRepository
import coupong.nbc.coupongwowdeal.exception.EmptyQuantityException
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import net.jqwik.api.Arbitraries
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.Collections
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CouponServiceDBTest @Autowired constructor(
    private val couponService: CouponService,
    private val couponUserJpaRepository: CouponUserJpaRepository,
    private val couponJpaRepository: CouponJpaRepository,
    private val userRepository: UserRepository
) {

    @BeforeEach
    fun setUp() {
        couponUserJpaRepository.deleteAll()
        couponJpaRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `1000명의 유저가 500개 재고의 쿠폰에 동시에 발급 시도를 했을 때 발급된 쿠폰은 500개인지 확인 - Coroutine`() {
        runBlocking {
            // given
            val testUserSize = 1000
            val testQuantity = 500
            var successCount = 0
            //var exceptionCount = 0
            var exceptionSet = mutableSetOf<Exception>()

            saveTestData(testUserSize, testQuantity)

            // when
            val jobs = List(testUserSize) { index ->
                CoroutineScope(Dispatchers.IO).async {
                    try {
                        couponService.issueCouponToUser(1L, index.toLong() + 1)
                        successCount++
                    } catch (e: Exception) {
                        //exceptionCount++
                        exceptionSet.add(e)
                        println("Error happens ${e.message}")
                    }
                }
            }

            jobs.joinAll()

            // then
            val findAllCouponUserSize = couponUserJpaRepository.findAll().size
            val finalCurrentQuantitiy = couponJpaRepository.findByIdOrNull(1L)?.currentQuantity
            println("successCount: $successCount")
            //println("exceptionCount: $exceptionCount")
            println("exceptionCount: ${exceptionSet.size}")
            println("couponUserJpaRepository.findAll().size: $findAllCouponUserSize")
            println("couponJpaRepository.findByIdOrNull(1L)?.currentQuantity: $finalCurrentQuantitiy")

            successCount shouldBe testQuantity
            //exceptionCount shouldBe testUserSize - testQuantity
            exceptionSet.size shouldBe testUserSize - testQuantity
            findAllCouponUserSize shouldBe testQuantity
            finalCurrentQuantitiy shouldBe 0
        }
    }

    @Test
    fun `510명의 유저가 500개 재고의 쿠폰에 동시에 발급 시도를 했을 때 EmptyQuantiyException이 발생하는지 확인 - Coroutine`() {
        runBlocking {
            // given
            val successQuantity = 500
            val exceptionQuantity = 10
            val testUserSize = successQuantity + exceptionQuantity
            var successCount = 0
            var exceptionCount = 0
            val exceptionList = mutableListOf<Exception>()

            saveTestData(testUserSize, successQuantity)

            // when
            val jobs = List(testUserSize) { index ->
                CoroutineScope(Dispatchers.IO).async {
                    try {
                        couponService.issueCouponToUser(1L, index.toLong() + 1)
                        successCount++
                    } catch (e: Exception) {
                        exceptionList.add(e)
                        exceptionCount++
                        println("Error happens ${e.message}")
                    }
                }
            }

            jobs.joinAll()

            // then
            val findAllCouponUserSize = couponUserJpaRepository.findAll().size
            val finalCurrentQuantity = couponJpaRepository.findByIdOrNull(1L)?.currentQuantity
            println("successCount: $successCount")
            println("couponUserJpaRepository.findAll().size: $findAllCouponUserSize")
            println("couponJpaRepository.findByIdOrNull(1L)?.currentQuantity: $finalCurrentQuantity")

            successCount shouldBe successQuantity
            exceptionCount shouldBe exceptionQuantity
            findAllCouponUserSize shouldBe successQuantity
            finalCurrentQuantity shouldBe 0

            exceptionList.size shouldBe exceptionQuantity
            exceptionList.forEach { it.shouldBeTypeOf<EmptyQuantityException>() }
        }
    }

    @Test
    fun `500명의 유저가 500개 재고의 쿠폰에 동시에 발급 시도를 했을 때 발급된 쿠폰은 500개인지 확인 - Coroutine`() {
        runBlocking {
            // given
            val testUserSize = 500
            val testQuantity = 500
            var successCount = 0

            saveTestData(testUserSize, testQuantity)

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
    fun `200명의 유저가 500개 재고의 쿠폰에 동시에 발급 시도를 했을 때 발급된 쿠폰은 200개인지 확인 - Coroutine`() {
        runBlocking {
            // given
            val testUserSize = 200
            val testQuantity = 500
            var successCount = 0

            saveTestData(testUserSize, testQuantity)

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

    fun saveTestData(testUserSize: Int, testQuantity: Int) {
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
    }

    @Test
    fun `100명의 유저가 50개 재고의 쿠폰을 동시에 발급 시도할 때 재고 소진 이후의 발급시도는 예외를 던진다_CyclicBarrier`() {
        // Arrange
        val testUserSize = 100
        val testQuantity = 50

        saveTestData(testUserSize, testQuantity)

        val barrier = CyclicBarrier(testUserSize)
        val service = Executors.newFixedThreadPool(testUserSize)
        val exceptions = Collections.synchronizedList(mutableListOf<Exception>())

        // Act
        repeat(testUserSize) { index ->
            service.execute {
                try {
                    barrier.await()
                    couponService.issueCouponToUser(1L, index + 1L)
                } catch (e: Exception) {
                    println(e.message)
                    exceptions.add(e)
                }
            }
        }
        service.shutdown()
        service.awaitTermination(1, TimeUnit.MINUTES)
        // Assert
        (testUserSize - testQuantity) shouldBe exceptions.size
        exceptions.forEach { exception ->
            assertTrue(exception is EmptyQuantityException)
        }
    }
}
