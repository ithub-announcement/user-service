package ru.itcollege.userservice.core.domain.providers

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
class JwtProvider(
  @Value("\${jwt.secret}") private val jwtAccessSecret: String,
  @Value("\${jwt.access-token-validity-hours}") private var jwtAccessValidityHours: Int
) {

  /**
   * ## decode
   *
   * Перевод строки secret в SecretKey.
   *
   * */

  private fun decode(): SecretKey {
    val decodedKey = Base64.getDecoder().decode(jwtAccessSecret.toByteArray())
    return SecretKeySpec(decodedKey, 0, decodedKey.size, "HmacSHA512")
  }

  /**
   * ## secret (var)
   *
   * Переменная с секретным ключом для построения JWT.
   * Объявляется в application.properties, но перед этим переведена в SecretKey.
   *
   * */

  private var secret: SecretKey = this.decode()

  /**
   * ## generate
   *
   * Генерация JWT токена.
   *
   * @param username
   * */

  fun generate(username: String): String {
    val now = Date()
    val expired = Date(now.time + this.jwtAccessValidityHours * 60 * 60 * 1000)
    return Jwts.builder().setIssuedAt(now).setExpiration(expired).setSubject(username)
      .signWith(SignatureAlgorithm.HS512, this.secret).compact()
  }

  /**
   * ## validate
   *
   * Валидация JWT токена.
   *
   * @param token
   * */

  fun validate(token: String): Boolean {
    try {
      val now = Date()
      val expired = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).body.expiration
      return !now.after(expired)
    } catch (error: ExpiredJwtException) {
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