package com.learning.orderfoodappsch3.data.database.mapper

import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import com.learning.orderfoodappsch3.data.database.relation.CartOrderFoodRelation
import com.learning.orderfoodappsch3.model.Cart
import com.learning.orderfoodappsch3.model.CartOrderFood

fun CartEntity?.toCart() = Cart(
    id = this?.id?:0,
    orderfoodId = this?.orderfoodId?:0,
    quantityCartItem = this?.quantityCartItem?:0,
    notes = this?.notes.orEmpty()
)

fun Cart?.toCartEntity() = CartEntity(
    id = this?.id,
    orderfoodId = this?.orderfoodId?:0,
    quantityCartItem = this?.quantityCartItem?:0,
    notes = this?.notes.orEmpty()
)

fun CartOrderFoodRelation.toCartOrderFood() = CartOrderFood(
    cart = this.cart.toCart(),
    orderFood = this.orderfood.toOrderFood()
)

fun List<CartEntity?>.toCartList() = this.map {
    it.toCart()
}

fun List<CartOrderFoodRelation>.toCartOrderFoodList() = this.map {
    it.toCartOrderFood()
}