package com.marcos.myspentapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcos.myspentapp.ui.database.AppDatabase
import com.marcos.myspentapp.ui.state.UserData
import kotlinx.coroutines.launch
import com.marcos.myspentapp.ui.database.setLogado
import com.marcos.myspentapp.ui.database.saveUser
import com.marcos.myspentapp.ui.database.UserSaved
import com.marcos.myspentapp.ui.database.getUser
import com.marcos.myspentapp.ui.database.updateUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class UserViewModel : ViewModel() {

    var userState by mutableStateOf(UserData())
        private set

    fun updateEmail(newEmail: String) {
        userState = userState.copy(email = newEmail)
    }

    fun updateSenha(newSenha: String) {
        userState = userState.copy(senha = newSenha)
    }
    fun updateCode(newCode: String) {
        userState = userState.copy(codeRescue = newCode)
    }

    fun updateGanhos(newGanhos: Double) {
        userState = userState.copy(ganhos = newGanhos)
    }

    fun toggleTheme(): Boolean {
        userState = userState.copy(
            darkTheme = !userState.darkTheme,
            initApp = true
        )
        return userState.darkTheme
    }

    fun setDarkTheme(enabled: Boolean) {
        if (!userState.initApp) {
            userState = userState.copy(
                darkTheme = enabled,
                initApp = true
            )
        }
    }

    // Funções ROOM
    private val _usuario = MutableStateFlow<UserSaved?>(null)
    val usuario = _usuario.asStateFlow()
    fun loadUser(
        context: Context,
        email: String,
    ) {
        viewModelScope.launch {
            val userAtivo = getUser(context, email)
            _usuario.value = userAtivo
        }
    }

    fun saveUserData(context: Context, user: UserSaved) {
        viewModelScope.launch {
            saveUser(context, user)
        }
    }

    fun updateUserData(context: Context, user: UserSaved) {
        viewModelScope.launch {
            updateUser(context, user)
        }
    }


    // Funções DataStore
    fun login(context: Context, email: String, senha: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val db = AppDatabase.getInstance(context)
            val user = db.userDao().getUserByEmail(email)

            if (user != null && user.senha == senha) {
                userState = userState.copy(
                    nome = user.name ?: "",
                    email = user.email,
                    senha = user.senha,
                    fotoUri = user.fotoUri,
                    codeRescue = user.codeRescue,
                    ganhos = user.ganhos,
                    darkTheme = user.darkTheme,
                    initApp = user.initApp
                )

                setLogado(context, true)
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun clearUser(context: Context) {
        viewModelScope.launch {
            val db = AppDatabase.getInstance(context)
            db.userDao().clear(userState.email)
        }
    }


}
