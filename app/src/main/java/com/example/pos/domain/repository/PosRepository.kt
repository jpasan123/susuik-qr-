package com.example.pos.domain.repository

import com.example.pos.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface PosRepository {
    suspend fun startSession(cashInHand: Double): Result<Session>
    suspend fun endSession(sessionId: String): Result<Session>
    suspend fun getCurrentSession(): Flow<Session?>
    suspend fun getAllSessions(): Flow<List<Session>>
}