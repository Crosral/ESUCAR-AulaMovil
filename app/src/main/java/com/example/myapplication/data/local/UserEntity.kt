package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val username: String, // Nombre de usuario o correo
    val passwordHash: String,
    val fullName: String,
    val role: String // "ESTUDIANTE" o "DOCENTE"
)