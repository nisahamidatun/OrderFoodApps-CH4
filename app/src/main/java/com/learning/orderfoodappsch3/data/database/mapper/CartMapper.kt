package com.learning.orderfoodappsch3.data.database.mapper

import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import com.learning.orderfoodappsch3.model.Cart

fun CartEntity?.toCart() = Cart(
    id = this?.id?:0,
    orderfoodId = this?.orderfoodId?:0,
    orderfoodName = this?.orderfoodName.orEmpty(),
    orderfoodPrice = this?.orderfoodPrice?: 0,
    orderfoodImgUrl = this?.orderfoodImgUrl.orEmpty(),
    quantityCartItem = this?.quantityCartItem?:0,
    notes = this?.notes.orEmpty()
)

fun Cart.toCartEntity() = CartEntity(
    id = this.id ?: 0,
    orderfoodId = this.orderfoodId?:0,
    orderfoodName = this.orderfoodName.orEmpty(),
    orderfoodPrice = this.orderfoodPrice ?: 0,
    orderfoodImgUrl = this.orderfoodImgUrl.orEmpty(),
    quantityCartItem = this.quantityCartItem?:0,
    notes = this.notes.orEmpty()
)

fun List<CartEntity?>.toCartList() = this.map {
    it.toCart()
}