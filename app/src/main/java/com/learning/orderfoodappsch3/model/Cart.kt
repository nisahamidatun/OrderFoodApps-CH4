package com.learning.orderfoodappsch3.model

data class Cart (
    var id: Int = 0,
    var orderfoodId: Int = 0,
    var quantityCartItem: Int = 0,
    var notes: String? = null
)