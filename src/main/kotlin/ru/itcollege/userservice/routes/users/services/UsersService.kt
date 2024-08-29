package ru.itcollege.userservice.routes.users.services

import org.springframework.stereotype.Service
import ru.itcollege.userservice.core.domain.providers.JwtProvider
import ru.itcollege.userservice.routes.users.models.entities.User
import ru.itcollege.userservice.routes.users.models.enums.URole
import ru.itcollege.userservice.routes.users.repositories.UsersRepository
import java.util.*

@Service
class UsersService(private var usersRepository: UsersRepository, private var jwtProvider: JwtProvider) {

  /**
   * ## findAll
   *
   * Получить список пользователей.
   *
   * */

  fun findAll(): MutableList<User> {
    return this.usersRepository.findAll()
  }

  /**
   * ## findUserByAccess
   *
   * Получить пользователя по **access** токену.
   *
   * @param access
   * */

  fun findByAccess(access: String): Optional<User?> {
    val uid = this.jwtProvider.read(access)
    return this.usersRepository.findById(uid)
  }

  /**
   * ## findUserByUID
   *
   * Получить пользователя по **uid**.
   *
   * @param uid
   * */

  fun findByUID(uid: String): Optional<User?> {
    return this.usersRepository.findById(uid)
  }

  /**
   * ## create
   *
   * Создать пользователя.
   *
   * @param uid
   * @param role
   * */

  private fun create(uid: String, role: URole = URole.DEFAULT): User {
    val current = User().apply {
      this.role = role
      this.uid = uid
      this.name = uid
    }
    return this.usersRepository.save(current)
  }

  /**
   * ## validate
   *
   * Проверка на отсутсвие пользователя в базе данных.
   *
   * @param uid
   * */

  fun validate(uid: String) {
    val current = this.usersRepository.findById(uid)
    if (current.isEmpty) {
      this.create(uid)
    } else return
  }
}