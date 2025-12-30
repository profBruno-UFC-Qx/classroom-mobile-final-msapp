package com.marcos.myspentapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.marcos.myspentapp.ui.state.CardUiState

class CardViewModel : ViewModel() {
    private val _cards = MutableStateFlow<List<CardUiState>>(emptyList())
    val cards: StateFlow<List<CardUiState>> = _cards

    fun addCard(card: CardUiState) {
        _cards.update { current ->
            current + card
        }
    }

    fun removeCard(id: String) {
        _cards.update { current ->
            current.filterNot { it.id == id }
        }
    }

    fun updateCard(id: String, updatedCard: CardUiState) {
        _cards.update { current ->
            current.map { card ->
                if (card.id == id) updatedCard else card
            }
        }
    }

    fun clear() {
        _cards.value = emptyList()
    }
}
