package com.marcos.myspentapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcos.myspentapp.data.models.Gastos
import kotlinx.coroutines.flow.MutableStateFlow
import com.marcos.myspentapp.ui.state.GastoUiState
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.marcos.myspentapp.data.database.AppDatabase
import com.marcos.myspentapp.data.models.TypeGasto
import com.marcos.myspentapp.data.repository.GastoRepository
import kotlinx.coroutines.flow.collectLatest

class GastoViewModel(
    private val repository: GastoRepository
) : ViewModel() {

    private val _gastos = MutableStateFlow<List<GastoUiState>>(emptyList())
    val gastos = _gastos.asStateFlow()

    var gastoState by mutableStateOf<List<Gastos>>(emptyList())
        private set

    private val _selectedIds = MutableStateFlow<Set<String>>(emptySet())
    val selectedIds = _selectedIds.asStateFlow()

    private val _editingCard = MutableStateFlow<Gastos?>(null)
    val editingCard = _editingCard.asStateFlow()

    // ---------- UI ----------
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

    fun openEdit(card: Gastos) {
        _editingCard.value = card
    }

    fun closeEdit() {
        _editingCard.value = null
    }

    // ---------- CRUD ----------
    fun addCard(card: Gastos, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.saveCard(card)
            onResult(true)
        }
    }

    fun updateCardInBase(card: Gastos, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.updateCard(card)
            onResult(true)
        }
    }

    fun removeSelected() {
        viewModelScope.launch {
            gastoState
                .filter { _selectedIds.value.contains(it.id) }
                .forEach { repository.deleteCard(it) }

            clearSelection()
        }
    }

    fun clearCards(userEmail: String) {
        viewModelScope.launch {
            repository.deleteCardsByUser(userEmail)
        }
    }

    // ---------- FLOW ----------
    fun loadCards(userEmail: String) {
        viewModelScope.launch {
            repository.getCardsByUser(userEmail).collectLatest { list ->

                gastoState = list

                _gastos.value = list.map { data ->
                    GastoUiState(
                        id = data.id,
                        title = data.title,
                        value = data.value,
                        type = TypeGasto.valueOf(data.type),
                        imageUri = data.imageUri?.toUri()
                    )
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class GastoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getInstance(context)
        val dao = db.gastoDao()
        val repo = GastoRepository(dao)
        return GastoViewModel(repo) as T
    }
}








