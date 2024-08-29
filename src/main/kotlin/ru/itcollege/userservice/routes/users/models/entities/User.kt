package ru.itcollege.userservice.routes.users.models.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import ru.itcollege.userservice.routes.users.models.enums.URole

@Entity(name = "users")
class User {
  @Id
  @Column(unique = true, nullable = false, updatable = false)
  lateinit var uid: String

  @Column(unique = false, nullable = true, updatable = true)
  lateinit var name: String

  @Column(unique = false, nullable = false, updatable = true)
  lateinit var role: URole
}