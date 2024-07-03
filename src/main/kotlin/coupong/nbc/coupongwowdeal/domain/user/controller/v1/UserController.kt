package coupong.nbc.coupongwowdeal.domain.user.controller.v1

import coupong.nbc.coupongwowdeal.domain.user.dto.v1.response.SignInResponse
import coupong.nbc.coupongwowdeal.domain.user.dto.v1.response.TokenCheckResponse
import coupong.nbc.coupongwowdeal.domain.user.service.v1.UserService
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/auth/token-test-generate")
    fun tokenTestGenerate(): ResponseEntity<SignInResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.tokenTestGenerate())
    }

    @GetMapping("/auth/token-check")
    fun tokenTestCheck(
        @AuthenticationPrincipal principal: UserPrincipal, httpServlet: HttpServletRequest
    ): ResponseEntity<TokenCheckResponse> {
        val accessToken =
            httpServlet.getHeader("Authorization") ?: throw IllegalArgumentException("Authorization header is required")

        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.tokenTestCheck(accessToken = accessToken, principal = principal))
    }
}