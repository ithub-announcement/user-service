package ru.itcollege.userservice.routes.users.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itcollege.userservice.routes.users.services.UsersService

@Tag(name = "Пользователи")
@RestController
@RequestMapping("/users")
class UsersController(private var usersService: UsersService) {
}