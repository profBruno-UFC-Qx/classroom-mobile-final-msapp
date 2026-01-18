package com.marcos.myspentapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// TABELA
@Entity(tableName = "users")
data class Users(
    @PrimaryKey val email: String,
    val name: String? = null,
    val senha: String? = null,
    val fotoUri: String? = null,
    val codeRescue: String = "",
    val ganhos: Double = 0.00,
    val darkTheme: Boolean = false,
    val initApp: Boolean = false,
)