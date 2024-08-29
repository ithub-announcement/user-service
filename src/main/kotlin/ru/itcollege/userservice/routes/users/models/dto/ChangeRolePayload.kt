package ru.itcollege.userservice.routes.users.models.dto

import ru.itcollege.userservice.routes.users.models.enums.URole

data class ChangeRolePayload(
  var role: URole
)