package com.marcos.myspentapp.data.repository

import androidx.compose.runtime.MutableState
import com.marcos.myspentapp.data.dao.GastoDao
import com.marcos.myspentapp.data.models.Gastos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class GastoRepository(
    private val dao: GastoDao,
) {
    suspend fun saveCard(card: Gastos) {
        dao.insertCard(card)
    }

    fun getCardsByUser(email: String): Flow<List<Gastos>> {
        return dao.getCardsByUser(email)
    }

    suspend fun updateCard(card: Gastos) {
        dao.updateCard(card)
    }

    suspend fun deleteCardsByUser(email: String) {
        dao.deleteCardsByUser(email)
    }

    suspend fun deleteCard(card: Gastos) {
        dao.deleteCard(card)
    }
}