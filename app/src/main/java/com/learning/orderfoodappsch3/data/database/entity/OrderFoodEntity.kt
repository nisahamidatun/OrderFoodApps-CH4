package com.learning.orderfoodappsch3.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "orderfood")
data class OrderFoodEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    @ColumnInfo(name = "img_food")
    val imgFood: String,
    @ColumnInfo(name = "food_name")
    val foodName: String,
    @ColumnInfo(name = "desc")
    val desc: String,
    @ColumnInfo(name = "food_price")
    val foodPrice: Int,

    )