package com.example.pos.data.remote.models.response

data class SessionResponse(
    val id: String,
    val cashInHand: Double,
    val startTime: String,
    val endTime: String? = null,
    val totalSales: Double = 0.0,
    val isActive: Boolean = true
)