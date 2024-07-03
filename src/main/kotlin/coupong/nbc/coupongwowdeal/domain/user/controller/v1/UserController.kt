package coupong.nbc.coupongwowdeal.domain.user.controller.v1

import coupong.nbc.coupongwowdeal.domain.user.service.v1.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService
)