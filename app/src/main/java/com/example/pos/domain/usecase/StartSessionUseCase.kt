package com.example.pos.domain.usecase

import com.example.pos.domain.model.Session
import com.example.pos.domain.repository.PosRepository
import javax.inject.Inject

class StartSessionUseCase @Inject constructor(
    private val repository: PosRepository
) {
    suspend operator fun invoke(cashInHand: Double): Result<Session> {
        return repository.startSession(cashInHand)
    }
}