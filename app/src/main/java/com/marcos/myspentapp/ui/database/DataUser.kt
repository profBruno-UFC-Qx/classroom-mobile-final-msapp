package com.marcos.myspentapp.ui.database

import android.content.Context
import androidx.room.*
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

// TABELA
@Entity(tableName = "users")
data class UserSaved(
    @PrimaryKey val email: String,
    val name: String?,
    val senha: String?,
    val fotoUri: String?,
    val codeRescue: String,
    val ganhos: Double,
    val darkTheme: Boolean,
    val initApp: Boolean
)

// ROOM
@Dao
interface UserDao{
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserSaved?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserSaved)

    @Update
    suspend fun updateUser(user: UserSaved)

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun clear(email: String)
}

// DATASTORE — CONFIGURAÇÃO
private val Context.dataStore by preferencesDataStore(name = "app_prefs")

object PrefKeys {
    val LOGADO = booleanPreferencesKey("is_logged")
}

// FUNÇÕES (DataStore)
suspend fun setLogado(context: Context, value: Boolean) {
    context.dataStore.edit { prefs ->
        prefs[PrefKeys.LOGADO] = value
    }
}



// FUNÇÕES (Room)
suspend fun saveUser(context: Context, user: UserSaved) {
    val db = AppDatabase.getInstance(context)
    db.userDao().insertUser(user)
}


suspend fun getUser(context: Context, email: String): UserSaved? {
    val db = AppDatabase.getInstance(context)
    return db.userDao().getUserByEmail(email)
}

suspend fun updateUser(context: Context, user: UserSaved) {
    val db = AppDatabase.getInstance(context)
    db.userDao().updateUser(user)
}