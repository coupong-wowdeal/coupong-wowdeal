package coupong.nbc.coupongwowdeal.domain.user.service.v1

import coupong.nbc.coupongwowdeal.domain.user.repository.v1.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
)
