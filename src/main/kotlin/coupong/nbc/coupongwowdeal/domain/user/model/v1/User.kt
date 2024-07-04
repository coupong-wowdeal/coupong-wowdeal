package coupong.nbc.coupongwowdeal.domain.user.model.v1

import coupong.nbc.coupongwowdeal.domain.common.BaseTimeEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "app_user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, length = 30)
    val username: String,

    @Column(length = 60)
    val password: String,

    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER,
) : BaseTimeEntity()