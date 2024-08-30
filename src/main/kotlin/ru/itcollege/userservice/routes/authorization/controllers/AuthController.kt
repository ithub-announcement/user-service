package ru.itcollege.userservice.routes.authorization.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itcollege.userservice.routes.authorization.models.dto.AuthPayload
import ru.itcollege.userservice.routes.authorization.models.dto.JwtPayload
import ru.itcollege.userservice.routes.authorization.services.AuthService

@Tag(name = "Авторизация")
@RestController
@RequestMapping("/auth")
class AuthController(private var authService: AuthService) {
  @PostMapping("/login")
  fun login(@RequestBody body: AuthPayload): ResponseEntity<JwtPayload> {
    return this.authService.login(body)
  }

  @PostMapping("/validate")
  fun validate(@RequestBody body: JwtPayload): ResponseEntity<Unit> {
    return this.authService.validate(body)
  }
}