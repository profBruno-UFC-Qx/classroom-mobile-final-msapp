package com.marcos.myspentapp.data.repository

import com.marcos.myspentapp.data.dao.UserDao
import com.marcos.myspentapp.data.models.Users
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val dao: UserDao,
) {

    // FUNÇÕES (Room)
    suspend fun saveUser(user: Users) {
        dao.insertUser(user)
    }

    fun getUser(email: String): Flow<Users?> {
        return dao.getUserByEmail(email)
    }

    suspend fun updateUser(
        emailAtual: String,
        novoEmail: String,
        novoNome: String?, novaSenha: String?,
        novaFotoUri: String?,
        novoCodeRescue: String,
        novoGanhos: Double,
        novoDarkTheme: Boolean,
        novoInitApp: Boolean
    ) {
        dao.updateUser(
            emailAtual,
            novoEmail,
            novoNome,
            novaSenha,
            novaFotoUri,
            novoCodeRescue,
            novoGanhos,
            novoDarkTheme,
            novoInitApp
        )
    }

    suspend fun clear(email: String) {
        dao.clear(email)
    }
}