package com.example.pos.utils.extensions

fun String.toCurrency(): String {
    return try {
        val amount = this.toDouble()
        "Rs. ${"%.2f".format(amount)}"
    } catch (e: NumberFormatException) {
        "Rs. 0.00"
    }
}

fun String.isValidAmount(): Boolean {
    return try {
        val amount = this.toDouble()
        amount >= 0
    } catch (e: NumberFormatException) {
        false
    }
}