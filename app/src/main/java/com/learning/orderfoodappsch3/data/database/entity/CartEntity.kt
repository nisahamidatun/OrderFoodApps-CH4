package com.learning.orderfoodappsch3.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "orderfood_id")
    var orderfoodId: Int = 0,
    @ColumnInfo(name = "orderfood_name")
    val orderfoodName: String,
    @ColumnInfo(name = "orderfood_price")
    val orderfoodPrice: Int,
    @ColumnInfo(name = "orderfood_img_url")
    val orderfoodImgUrl: String,
    @ColumnInfo(name = "quantity_cart_item")
    var quantityCartItem: Int = 0,
    @ColumnInfo(name = "notes")
    var notes: String? = null
)