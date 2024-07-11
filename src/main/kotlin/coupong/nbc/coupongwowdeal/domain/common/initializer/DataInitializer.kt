package coupong.nbc.coupongwowdeal.domain.common.initializer

import coupong.nbc.coupongwowdeal.domain.timedeal.service.v1.TimeDealService
import coupong.nbc.coupongwowdeal.domain.user.service.v1.UserService
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class DataInitializer(
    private val userService: UserService,
    private val timeDealService: TimeDealService,
) {
    @Bean
    @Profile("!test")
    fun initData() = ApplicationRunner {
        // runBlocking {
        //     val userSize = 3000
        //     val couponQuantity = 1000
        //     val userJobs = (1..userSize).map { i ->
        //         CoroutineScope(Dispatchers.IO).async {
        //             userService.signUp(SignUpRequest("user$i", "password", "password"))
        //         }
        //     }
        //     userJobs.joinAll()
        //
        //     // val jwtTokens = mutableListOf<String>()
        //     // val tokenJobs = (1..userSize).map { i ->
        //     //     CoroutineScope(Dispatchers.IO).async {
        //     //         val response = userService.signIn(SignInRequest("user$i", "password"))
        //     //         response.accessToken
        //     //     }
        //     // }
        //     //
        //     // jwtTokens.addAll(tokenJobs.awaitAll())
        //     //
        //     // File("jwt_tokens.txt").writeText(jwtTokens.joinToString("\n"))
        //
        //     timeDealService.createTimeDeal(
        //         UserPrincipal(1, setOf("ROLE_ADMIN")),
        //         CreateTimeDealRequest(
        //             name = "testTimeDeal",
        //             openedAt = LocalDateTime.now(),
        //             closedAt = LocalDateTime.now().plusDays(7),
        //             couponName = "testCoupon",
        //             couponExpiredAt = LocalDateTime.now().plusDays(7),
        //             couponDiscountPrice = 1000,
        //             couponTotalQuantity = couponQuantity
        //         )
        //     )
        // }
    }
}