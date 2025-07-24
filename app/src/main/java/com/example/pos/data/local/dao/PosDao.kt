package com.example.pos.data.local.dao

import androidx.room.*
import com.example.pos.data.local.database.entities.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PosDao {
    @Query("SELECT * FROM sessions WHERE isActive = 1 LIMIT 1")
    fun getCurrentSession(): Flow<SessionEntity?>
    
    @Query("SELECT * FROM sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)
    
    @Update
    suspend fun updateSession(session: SessionEntity)
    
    @Query("UPDATE sessions SET isActive = 0, endTime = :endTime WHERE id = :sessionId")
    suspend fun endSession(sessionId: String, endTime: Long)
}