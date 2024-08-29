package ru.itcollege.userservice.routes.users.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
  fun getUserByAccess(request: HttpServletRequest): Optional<User?> {
    return this.usersService.findByAccess(request.getHeader("Authorization"))
  }

  @GetMapping("/profile/{uid}")
  fun getUserByUID(@PathVariable uid: String): Optional<User?> {
    return this.usersService.findByUID(uid)
  }

}