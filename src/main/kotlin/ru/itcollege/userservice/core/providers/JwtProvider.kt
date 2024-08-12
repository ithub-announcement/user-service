package ru.itcollege.userservice.core.providers

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * # JwtProvider
 *
 * Провайдер для работы с JWT токенами.
 *
 * @author Дмитрий Чехонадских <loseex@vk.com>
 * */

@Component
class JwtProvider(@Value("\${jwt.secret}") private val jwtAccessSecret: String) {

  /**
   * ## decode
   *
   * Перевод строки secret в SecretKey.
   *
   * @param secret
   * */

  private fun decode(secret: String): SecretKey {
    val decodeKey = Base64.getDecoder().decode(secret)
    return SecretKeySpec(decodeKey, 0, decodeKey.size, "HmacSHA512")
  }

  /**
   * ## secret (var)
   *
   * Переменная с секретным ключом для построения JWT.
   * Объявляется в application.properties, но перед этим переведена в SecretKey.
   *
   * */

  private var secret: SecretKey = this.decode(this.jwtAccessSecret)

  /**
   * ## generate
   *
   * Генерация JWT токена.
   *
   * @param username
   * @param validity
   * */

  fun generate(username: String, validity: Long): String {
    val now: Date = Date()
    val expired: Date = Date(now.time + validity * 60 * 60 * 1000)
    return Jwts.builder().setIssuedAt(now).setExpiration(expired).setSubject(username)
      .signWith(SignatureAlgorithm.HS512, this.secret).compact()
  }

  /**
   * ## isValidate
   *
   * Валидация JWT токена.
   *
   * @param token
   * */

  fun isValidate(token: String): Boolean {
    try {
      Jwts.parser().setSigningKey(this.secret).parseClaimsJwt(token)
      return true
    } catch (error: ExpiredJwtException) {
      return false
    } catch (error: SignatureException) {
      return false
    }
  }

  /**
   * ## read
   *
   * Прочитать JWT токен, и получить uid пользователя.
   *
   * @param token
   * */

  fun read(token: String): String {
    val claims: Claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).body
    return claims.subject
  }
}