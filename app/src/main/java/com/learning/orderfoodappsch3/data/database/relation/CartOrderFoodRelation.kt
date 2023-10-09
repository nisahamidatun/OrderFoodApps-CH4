package com.learning.orderfoodappsch3.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import com.learning.orderfoodappsch3.data.database.entity.OrderFoodEntity

data class CartOrderFoodRelation (
    @Embedded
    val cart: CartEntity,
    @Relation (parentColumn = "orderfood_id", entityColumn = "id")
    val orderfood: OrderFoodEntity
)