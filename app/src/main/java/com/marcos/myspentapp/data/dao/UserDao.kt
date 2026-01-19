package com.marcos.myspentapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marcos.myspentapp.data.models.Users
import kotlinx.coroutines.flow.Flow

// ROOM
@Dao
interface UserDao{
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): Flow<Users?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Users)

    @Query("""
        UPDATE users SET
            email = :novoEmail,
            name = :novoNome,
            senha = :novaSenha,
            fotoUri = :novaFotoUri,
            codeRescue = :novoCodeRescue,
            ganhos = :novoGanhos,
            darkTheme = :novoDarkTheme,
            initApp = :novoInitApp
        WHERE email = :emailAtual
""")
    suspend fun updateUser(
        emailAtual: String,
        novoEmail: String,
        novoNome: String?,
        novaSenha: String?,
        novaFotoUri: String?,
        novoCodeRescue: String,
        novoGanhos: Double,
        novoDarkTheme: Boolean,
        novoInitApp: Boolean
    )

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun clear(email: String)

}