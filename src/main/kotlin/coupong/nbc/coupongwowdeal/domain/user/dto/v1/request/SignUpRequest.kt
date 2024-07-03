package coupong.nbc.coupongwowdeal.domain.user.dto.v1.request

import coupong.nbc.coupongwowdeal.domain.user.model.v1.User
import org.springframework.security.crypto.password.PasswordEncoder

data class SignUpRequest(
    val username: String,
    val password: String,
    val confirmPassword: String
) {
    fun toEntity(passwordEncoder: PasswordEncoder): User {
        return User(
            username = username,
            password = passwordEncoder.encode(password)
        )
    }
}
