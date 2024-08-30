package ru.itcollege.userservice.routes.authorization.services

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.itcollege.userservice.core.domain.providers.JwtProvider
import ru.itcollege.userservice.routes.authorization.models.dto.AuthPayload
import ru.itcollege.userservice.routes.authorization.models.dto.JwtPayload
import ru.itcollege.userservice.routes.users.services.UsersService

@Service
class AuthService(
  private var jwtProvider: JwtProvider,
  private var authenticationManager: AuthenticationManager,
  private var usersService: UsersService
) {

  /**
   * ## login
   *
   * Авторизация в системе.
   *
   * @param payload
   * */

  fun login(payload: AuthPayload): ResponseEntity<JwtPayload> {
    val authentication =
      this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken(payload.username, payload.password))
    SecurityContextHolder.getContext().authentication = authentication
    this.usersService.validate(authentication.name)
    return ResponseEntity.ok().body(JwtPayload(this.jwtProvider.generate(authentication.name)))
  }

  /**
   * ## validate
   *
   * Валидация токена авторизации.
   *
   * @param payload
   * */

  fun validate(payload: JwtPayload): ResponseEntity<Unit> {
    if (this.jwtProvider.isValidate(payload.access)) {
      return ResponseEntity.badRequest().build()
    }
    return ResponseEntity.ok().build()
  }
}