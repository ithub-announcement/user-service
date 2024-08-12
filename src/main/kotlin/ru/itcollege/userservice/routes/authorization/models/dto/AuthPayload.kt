package ru.itcollege.userservice.routes.authorization.models.dto

data class AuthPayload(
  var username: String,
  var password: String
)
