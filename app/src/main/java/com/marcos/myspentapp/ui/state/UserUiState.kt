package com.marcos.myspentapp.ui.state

data class UserUiState(
    val nome: String = "Usuário",
    val email: String = "",
    val senha: String = "",
    val fotoUri: String? = null,
    val codeRescue: String = "",
    val ganhos: Double = 0.00,
    val darkTheme: Boolean = false,
    val initApp: Boolean = false
)


