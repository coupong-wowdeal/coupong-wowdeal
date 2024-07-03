package coupong.nbc.coupongwowdeal.domain.user.repository.v1

import coupong.nbc.coupongwowdeal.domain.user.model.v1.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>