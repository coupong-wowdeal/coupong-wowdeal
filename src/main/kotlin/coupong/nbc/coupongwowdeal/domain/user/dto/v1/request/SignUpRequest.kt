package coupong.nbc.coupongwowdeal.domain.user.dto.v1.request

import coupong.nbc.coupongwowdeal.domain.user.model.v1.User
import org.springframework.security.crypto.password.PasswordEncoder

data class SignUpRequest(
    val username: String,
    val password: String,
    val confirmPassword: String
) {
    init {
        validate()
    }

    private fun validate() {
        require(username.length > 3) { "Username must be longer than 3" }
        require(username.matches(Regex("^[a-zA-Z0-9]*$"))) { "Username only alphabet and number allowed" }
        require(password.length > 4) { "Password must be longer than 4" }
        require(!password.contains(username)) { "Password must not contain username" }
        require(password == confirmPassword) { "Password must be equal to confirmPassword" }
    }

    fun toEntity(passwordEncoder: PasswordEncoder): User {
        return User(
            username = username,
            password = passwordEncoder.encode(password)
        )
    }
}
