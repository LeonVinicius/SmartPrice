package com.senac.smartprice.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "usuario", indices = [Index(value = ["email"], unique = true)])
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val email: String,
    val perfil: String,      // CONSUMIDOR ou COMERCIANTE
    val bairro: String,
    val senhaHash: String,
    val salt: String
)
