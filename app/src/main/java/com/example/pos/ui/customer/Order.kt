package com.example.pos.ui.customer

import com.example.pos.ui.pos.CartItem

data class Order(
    val id: String,
    val customerId: String,
    val items: List<CartItem>,
    val total: Double
)
