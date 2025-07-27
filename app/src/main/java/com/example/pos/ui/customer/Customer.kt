package com.example.pos.ui.customer

data class Customer(
    val id: String,
    var name: String,
    var phone: String = "",
    var email: String = ""
)
