package ru.itcollege.userservice.routes.users.services

import org.springframework.stereotype.Service
import ru.itcollege.userservice.routes.users.repositories.UsersRepository

@Service
class UsersService(private var usersRepository: UsersRepository) {
}