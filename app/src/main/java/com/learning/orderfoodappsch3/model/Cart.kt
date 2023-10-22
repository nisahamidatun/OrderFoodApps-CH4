package com.learning.orderfoodappsch3.model

data class Cart(
    var id: Int ? = null,
    var orderfoodId: Int = 0,
    val orderfoodName: String,
    val orderfoodPrice: Int,
    val orderfoodImgUrl: String,
    var quantityCartItem: Int = 0,
    var notes: String? = null,
)