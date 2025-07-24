package com.example.pos.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey
    val id: String,
    val cashInHand: Double,
    val startTime: Long,
    val endTime: Long? = null,
    val totalSales: Double = 0.0,
    val isActive: Boolean = true
)