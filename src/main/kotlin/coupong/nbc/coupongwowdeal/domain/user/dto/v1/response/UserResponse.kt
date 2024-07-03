package coupong.nbc.coupongwowdeal.domain.user.dto.v1.response

import coupong.nbc.coupongwowdeal.domain.user.model.v1.User

data class UserResponse(
    val id: Long,
    val username: String,
    val role: String
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                username = user.username,
                role = user.role.name
            )
        }
    }
}
