package com.learning.orderfoodappsch3.data.database.mapper

import com.learning.orderfoodappsch3.data.database.entity.OrderFoodEntity
import com.learning.orderfoodappsch3.model.OrderFood

fun OrderFoodEntity?.toOrderFood() = OrderFood(
    id = this?.id?: 0,
    imgFood = this?.imgFood.orEmpty(),
    foodName = this?.foodName.orEmpty(),
    desc = this?.desc.orEmpty(),
    foodPrice = this?.foodPrice?: 0,
)

fun OrderFood?.toOrderFoodEntity() = OrderFoodEntity(
    imgFood = this?.imgFood.orEmpty(),
    foodName = this?.foodName.orEmpty(),
    desc = this?.desc.orEmpty(),
    foodPrice = this?.foodPrice?: 0,
).apply {
    this@toOrderFoodEntity?.id?.let {
        this.id = this@toOrderFoodEntity.id
    }
}

fun List<OrderFoodEntity?>.toOrderFoodList() = this.map {
    it.toOrderFood()
}

fun List<OrderFood?>.toOrderFoodEntity() = this.map {
    it.toOrderFoodEntity()
}