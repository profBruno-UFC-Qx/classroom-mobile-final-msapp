package com.marcos.myspentapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.marcos.myspentapp.data.database.AppDatabase
import com.marcos.myspentapp.ui.state.UserUiState
import kotlinx.coroutines.launch
import com.marcos.myspentapp.data.repository.UserRepository
import com.marcos.myspentapp.data.models.Users
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var userState by mutableStateOf(UserUiState())
        private set

    // UI STATE Functions
    fun updateNome(newNome: String) {
        userState = userState.copy(nome = newNome)
    }
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


    // ROOM Functions
    private val _usuario = MutableStateFlow<Users?>(null)
    val usuario = _usuario.asStateFlow()

    fun loadUser(email: String) {
        viewModelScope.launch {
            repository.getUser(email).collect { user ->
                _usuario.value = user
            }
        }
    }

    fun saveUser(user: Users) {
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }

    fun updateUser(
        emailAtual: String,
        novoEmail: String,
        novoNome: String?,
        novaSenha: String?,
        novaFotoUri: String?,
        novoCodeRescue: String,
        novoGanhos: Double,
        novoDarkTheme: Boolean,
        novoInitApp: Boolean
    ) {
        viewModelScope.launch {
            repository.updateUser(
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
            loadUser(novoEmail)
        }
    }

    // LOGIN
    fun login(email: String, senha: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUser(email).first()

            if (user?.senha == senha) {
                userState = userState.copy(
                    nome = user.name.orEmpty(),
                    email = user.email,
                    senha = user.senha,
                    fotoUri = user.fotoUri,
                    codeRescue = user.codeRescue,
                    ganhos = user.ganhos,
                    darkTheme = user.darkTheme,
                    initApp = user.initApp
                )
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            repository.clear(userState.email)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        val dao = db.userDao()
        val repo = UserRepository(dao)
        return UserViewModel(repo) as T
    }
}