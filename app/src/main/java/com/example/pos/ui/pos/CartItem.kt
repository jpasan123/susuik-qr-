package com.example.pos.ui.pos

data class CartItem(
    val product: Product,
    var quantity: Int,
    var variant: String = "Variant Name",
    var isExpanded: Boolean = false,
    val category: String = "Food" // "Food" or "Drinks"
)