package ru.itcollege.userservice.routes.users.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.*
import ru.itcollege.userservice.routes.users.models.dto.ChangeNamePayload
import ru.itcollege.userservice.routes.users.models.dto.ChangeRolePayload
import ru.itcollege.userservice.routes.users.models.entities.User
import ru.itcollege.userservice.routes.users.services.UsersService
import java.util.*

@Tag(name = "Пользователи")
@RestController
@RequestMapping("/users")
class UsersController(private var usersService: UsersService) {

  @GetMapping()
  fun getAllUsers(): MutableList<User> {
    return this.usersService.findAll()
  }

  @GetMapping("/profile")
  fun getUserByAccess(request: HttpServletRequest): User {
    return this.usersService.findByAccess(request.getHeader("Authorization"))
  }

  @GetMapping("/profile/{uid}")
  fun getUserByUID(@PathVariable uid: String): Optional<User?> {
    return this.usersService.findByUID(uid)
  }

  @PutMapping("/profile/{uid}/role/change")
  fun changeRole(@PathVariable uid: String, @RequestBody body: ChangeRolePayload, request: HttpServletRequest): User {
    return this.usersService.changeRole(uid, body.role, request)
  }

  @PutMapping("/profile/{uid}/name/change")
  fun changeName(@PathVariable uid: String, @RequestBody body: ChangeNamePayload, request: HttpServletRequest): User {
    return this.usersService.changeName(uid, body.name, request)
  }

}