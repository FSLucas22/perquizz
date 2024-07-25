package com.perquizz.perquizz.users.repositories

import com.perquizz.perquizz.users.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long>
