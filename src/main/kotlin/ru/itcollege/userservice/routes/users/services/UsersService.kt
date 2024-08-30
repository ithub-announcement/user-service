package ru.itcollege.userservice.routes.users.services

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
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

  fun findAll(): ResponseEntity<MutableList<User>> {
    val array = this.usersRepository.findAll()
    return ResponseEntity.ok().body(array)
  }

  /**
   * ## findUserByAccess
   *
   * Получить пользователя по **access** токену.
   *
   * @param access
   * */

  fun findByAccess(access: String): ResponseEntity<Optional<User>> {
    val uid = this.jwtProvider.read(access)
    val current = this.usersRepository.findById(uid)
    return ResponseEntity.ok().body(current)
  }

  /**
   * ## findUserByUID
   *
   * Получить пользователя по **uid**.
   *
   * @param uid
   * */

  fun findByUID(uid: String): ResponseEntity<Optional<User>> {
    val current = this.usersRepository.findById(uid)
    return ResponseEntity.ok().body(current)
  }

  /**
   * ## create
   *
   * Создать пользователя.
   *
   * @param uid
   * @param role
   * */

  private fun create(uid: String, role: URole = URole.DEFAULT): ResponseEntity<User> {
    val current = User().apply {
      this.role = role
      this.uid = uid
      this.name = uid
    }
    return ResponseEntity.status(201).body(this.usersRepository.save(current))
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

  /**
   * ## changeUserProperty
   *
   * Метод чтобы изменить данные пользователя.
   *
   * @param uid
   * @param update
   * */

  private fun changeUserProperty(uid: String, update: (User) -> Unit): ResponseEntity<User> {
    val current = this.usersRepository.findById(uid).get().apply(update)
    return ResponseEntity.ok().body(this.usersRepository.save(current))
  }

  /**
   * ## changeRole
   *
   * Изменить роль у пользователя.
   *
   * @param uid
   * @param role
   * */

  fun changeRole(uid: String, role: URole, request: HttpServletRequest): ResponseEntity<User> {
    val current = this.findByAccess(request.getHeader("Authorization"))
    require(current.body?.get()?.role == URole.ADMIN) { ResponseEntity.status(401).body("У вас нет прав на это.") }
    return this.changeUserProperty(uid) { it.role = role }
  }

  /**
   * ## changeName
   *
   * Изменить имя пользователя.
   *
   * @param uid
   * @param name
   * */

  fun changeName(uid: String, name: String, request: HttpServletRequest): ResponseEntity<User> {
    val current = this.findByAccess(request.getHeader("Authorization"))
    require(current.body?.get()?.role == URole.ADMIN || current.body?.get()?.uid !== uid) { ResponseEntity.status(401).body("У вас нет прав на это.") }
    return this.changeUserProperty(uid) { it.name = name }
  }
}