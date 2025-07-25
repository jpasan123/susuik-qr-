package com.example.pos.ui.pos

import androidx.lifecycle.ViewModel
import com.example.pos.domain.usecase.StartSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    suspend fun startSession(cashAmount: Double): Boolean {
        return try {
            _uiState.value = _uiState.value.copy(isLoading = true)
            startSessionUseCase(cashAmount) // This should be a suspend function
            _uiState.value = _uiState.value.copy(isLoading = false)
            true
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = e.message ?: "Failed to start session"
            )
            false
        }
    }
}

data class PosUiState(
    val cashInHand: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)