package com.ifsp.crossfitwod.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ifsp.crossfitwod.data.model.Resultado
import com.ifsp.crossfitwod.data.model.Wod
import com.ifsp.crossfitwod.data.repository.WodRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WodViewModel(private val repository: WodRepository) : ViewModel() {

    val wods: StateFlow<List<Wod>> = repository.getWods()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addWod(wod: Wod) {
        viewModelScope.launch {
            repository.addWod(wod)
        }
    }

    fun updateWod(wod: Wod) {
        viewModelScope.launch {
            repository.updateWod(wod)
        }
    }

    fun deleteWod(wodId: String) {
        viewModelScope.launch {
            repository.deleteWod(wodId)
        }
    }

    fun addResult(wodId: String, resultado: Resultado) {
        viewModelScope.launch {
            repository.addResult(wodId, resultado)
        }
    }
}

class WodViewModelFactory(private val repository: WodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
