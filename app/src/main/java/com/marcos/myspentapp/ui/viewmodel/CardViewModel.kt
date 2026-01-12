package com.marcos.myspentapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcos.myspentapp.ui.database.AppDatabase
import com.marcos.myspentapp.ui.database.CardData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import com.marcos.myspentapp.ui.state.CardUiState
import com.marcos.myspentapp.ui.state.TypeGasto
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class CardViewModel : ViewModel() {
    private val _cards = MutableStateFlow<List<CardUiState>>(emptyList())
    var cardState by mutableStateOf<List<CardData>>(emptyList())
        private set
    val cards = _cards.asStateFlow()
    private val _selectedIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedIds = _selectedIds.asStateFlow()
    private val _editingCard = MutableStateFlow<CardUiState?>(null)
    val editingCard = _editingCard.asStateFlow()

    fun addCard(card: CardUiState) {
        _cards.update { current ->
            current + card
        }
    }

    fun updateCard(id: String, updatedCard: CardUiState) {
        _cards.update { current ->
            current.map { card ->
                if (card.id == id) updatedCard else card
            }
        }
    }

    fun toggleSelection(id: String) {
        _selectedIds.value =
            if (_selectedIds.value.contains(id))
                _selectedIds.value - id
            else
                _selectedIds.value + id
    }

    fun clearSelection() {
        _selectedIds.value = emptySet()
    }

    fun removeSelected(context: Context, userEmail: String) {
        viewModelScope.launch {
            val db = AppDatabase.getInstance(context)

            // Pega os cards no banco
            val cardsToDelete = db.cardDao().getCardsByUser(userEmail)
                .filter { _selectedIds.value.contains(it.id) }

            // Deleta um por um
            cardsToDelete.forEach { db.cardDao().deleteCard(it) }

            // Recarrega da Room
            loadCards(context, userEmail)

            // Limpa seleção
            clearSelection()
        }
    }

    fun openEdit(card: CardUiState) {
        _editingCard.value = card
    }

    fun closeEdit() {
        _editingCard.value = null
    }

    fun addCard(context: Context, card: CardData, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val db = AppDatabase.getInstance(context)
            db.cardDao().insertCard(card)

            loadCards(context, card.userEmail)
            onResult(true)
        }
    }

    fun updateCardInBase(
        context: Context,
        updatedCard: CardData,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val db = AppDatabase.getInstance(context)

            // Atualiza no banco
            db.cardDao().updateCard(updatedCard)

            // Recarrega todos os cards daquele usuário
            loadCards(context, updatedCard.userEmail)

            onResult(true)
        }
    }

    fun loadCards(context: Context, userId: String) {
        viewModelScope.launch {
            val db = AppDatabase.getInstance(context)
            val list = db.cardDao().getCardsByUser(userId)

            cardState = list // Mantém se quiser

            _cards.value = list.map { data ->
                CardUiState(
                    id = data.id,
                    title = data.title,
                    value = data.value,
                    type = TypeGasto.valueOf(data.type),
                    imageUri = data.imageUri?.toUri()
                )
            }
        }
    }

    fun clearCards(context: Context, userId: String) {
        viewModelScope.launch {
            val db = AppDatabase.getInstance(context)
            db.cardDao().deleteCardsByUser(userId)
        }
    }
}








