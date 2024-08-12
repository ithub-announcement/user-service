package ru.itcollege.userservice.core.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory
import org.springframework.security.ldap.DefaultSpringSecurityContextSource
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
class SecurityConfiguration {
  @Value("\${ldap.url}")
  private lateinit var ldapUrl: String

  @Bean
  fun securityFilterChain(http: HttpSecurity): DefaultSecurityFilterChain? {
    return http
      .csrf { obj: AbstractHttpConfigurer<*, *> -> obj.disable() }
      .cors { obj: AbstractHttpConfigurer<*, *> -> obj.disable() }
      .logout { logout -> logout.logoutRequestMatcher(AntPathRequestMatcher("/logout")) }
      .sessionManagement { configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .authorizeHttpRequests { authorize ->
        authorize
          .requestMatchers(HttpMethod.POST, "/api/v1/auth/login")
          .permitAll()
          .requestMatchers(HttpMethod.POST, "/api/user/validate")
          .permitAll()
          .anyRequest()
          .permitAll()
      }
      .httpBasic { obj: AbstractHttpConfigurer<*, *> -> obj.disable() }
      .formLogin { obj: AbstractHttpConfigurer<*, *> -> obj.disable() }
      .build()
  }

  @Bean
  fun authenticationManager(): AuthenticationManager? {
    val contextSource = DefaultSpringSecurityContextSource(this.ldapUrl)
    contextSource.userDn = "cn=users,dc=it-college,dc=ru"
    contextSource.password = "password"
    contextSource.afterPropertiesSet()
    val factory = LdapBindAuthenticationManagerFactory(contextSource)
    factory.setUserDnPatterns("uid={0},cn=users,dc=it-college,dc=ru")
    factory.setUserSearchBase("cn=users,dc=it-college,dc=ru")
    factory.setUserSearchFilter("(uid={0})")
    factory.setUserDetailsContextMapper(InetOrgPersonContextMapper())
    return factory.createAuthenticationManager()
  }
}