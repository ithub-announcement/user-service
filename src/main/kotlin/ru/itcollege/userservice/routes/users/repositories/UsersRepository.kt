package ru.itcollege.userservice.routes.users.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.itcollege.userservice.routes.users.models.entities.User

@Repository
interface UsersRepository : JpaRepository<User, String> {
}