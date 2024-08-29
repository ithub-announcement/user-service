package ru.itcollege.userservice.routes.authorization.services

import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import ru.itcollege.userservice.core.domain.providers.JwtProvider
import ru.itcollege.userservice.routes.authorization.models.dto.AuthPayload
import ru.itcollege.userservice.routes.authorization.models.dto.JwtPayload

@Service
class AuthService(private var jwtProvider: JwtProvider, private var authenticationManager: AuthenticationManager) {

  /**
   * ## login
   *
   * Авторизация в системе.
   *
   * @param payload
   * */

  fun login(payload: AuthPayload): JwtPayload {
    val authentication =
      this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken(payload.username, payload.password))
    SecurityContextHolder.getContext().authentication = authentication
    return JwtPayload(this.jwtProvider.generate(authentication.name))
  }

  /**
   * ## validate
   *
   * Валидация токена авторизации.
   *
   * @param payload
   * */

  fun validate(payload: JwtPayload): ResponseEntity<Unit> {
    if (!this.jwtProvider.validate(payload.access)) {
      return ResponseEntity.badRequest().build()
    }
    return ResponseEntity.ok().build()
  }
}