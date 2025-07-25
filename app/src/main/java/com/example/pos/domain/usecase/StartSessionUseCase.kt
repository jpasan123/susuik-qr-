package com.example.pos.domain.usecase

import com.example.pos.domain.repository.PosRepository
import javax.inject.Inject

class StartSessionUseCase @Inject constructor(
    private val repository: PosRepository
) {
    suspend operator fun invoke(amount: Double) {
        // Add your session starting logic here
        // For now just simulate a network call
        kotlinx.coroutines.delay(500) // Simulate network delay
        repository.startSession(amount)
    }
}