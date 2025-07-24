package com.example.pos.domain.model

import java.util.Date

data class Session(
    val id: String,
    val cashInHand: Double,
    val startTime: Date,
    val endTime: Date? = null,
    val totalSales: Double = 0.0,
    val isActive: Boolean = true
)