package ru.itcollege.userservice.core.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.core.support.LdapContextSource

@Configuration
class LdapConfiguration {

  @Value("\${ldap.url}")
  private lateinit var ldapUrl: String

  @Bean
  fun contextSource(): LdapContextSource {
    val contextSource = LdapContextSource()
    contextSource.setUrl(this.ldapUrl)
    contextSource.setBase("dc=example,dc=com")
    contextSource.userDn = "cn=admin,dc=example,dc=com"
    contextSource.afterPropertiesSet()
    return contextSource
  }

  @Bean
  fun ldapTemplate(contextSource: LdapContextSource): LdapTemplate {
    return LdapTemplate(contextSource)
  }
}