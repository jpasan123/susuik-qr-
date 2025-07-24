package com.example.pos.ui.pos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pos.domain.usecase.StartSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PosViewModel @Inject constructor(
    private val startSessionUseCase: StartSessionUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(PosUiState())
    val uiState: StateFlow<PosUiState> = _uiState
    
    fun updateCashInHand(value: String) {
        _uiState.value = _uiState.value.copy(cashInHand = value)
    }
    
    fun startSession() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val amount = _uiState.value.cashInHand.toDoubleOrNull() ?: 0.0
                startSessionUseCase(amount)
                // Navigate to main POS screen or show success
            } catch (e: Exception) {
                // Handle error
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}

data class PosUiState(
    val cashInHand: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)