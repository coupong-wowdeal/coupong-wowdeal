package coupong.nbc.coupongwowdeal.domain.user.service.v1

import coupong.nbc.coupongwowdeal.domain.user.dto.v1.response.SignInResponse
import coupong.nbc.coupongwowdeal.domain.user.dto.v1.response.TokenCheckResponse
import coupong.nbc.coupongwowdeal.domain.user.model.v1.User
import coupong.nbc.coupongwowdeal.domain.user.model.v1.UserRole
import coupong.nbc.coupongwowdeal.domain.user.repository.v1.UserRepository
import coupong.nbc.coupongwowdeal.infra.security.UserPrincipal
import coupong.nbc.coupongwowdeal.infra.security.jwt.JwtPlugin
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
) {
    fun tokenTestGenerate(): SignInResponse {
        return (userRepository.findByIdOrNull(1) ?: userRepository.save(
            User(
                username = "testAdmin",
                password = "12345678",
                role = UserRole.ADMIN
            )
        ))
            .let { SignInResponse.from(jwtPlugin = jwtPlugin, user = it) }
    }

    fun tokenTestCheck(accessToken: String, principal: UserPrincipal): TokenCheckResponse {
        val userId = principal.id
        val role = principal.role

        return TokenCheckResponse.from(userId, role)
    }
}
