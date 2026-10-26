package com.cao.repairshop.user.infra.persistence.models

import com.cao.repairshop.user.domain.entities.UserRole
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "tb_user")
class UserEntity(
    @Id
    @Column(name = "id_tb_user")
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, length = 150)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "function", nullable = false, length = 50)
    var function: UserRole,

    @Column(nullable = false, unique = true, length = 14)
    var cpf: String,

    @Column(nullable = false, unique = true, length = 255)
    var email: String,

    @Column(length = 20)
    var phone: String? = null,

    @Column(nullable = false, length = 255)
    var password: String
) {
    override fun equals(other: Any?): Boolean = other is UserEntity && id == other.id
    override fun hashCode(): Int = id.hashCode()
    override fun toString(): String = "UserDataModel(id=$id, email=$email)"
}
