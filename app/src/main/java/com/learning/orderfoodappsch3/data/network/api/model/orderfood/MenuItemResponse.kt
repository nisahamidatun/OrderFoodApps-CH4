package com.learning.orderfoodappsch3.data.network.api.model.orderfood

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.learning.orderfoodappsch3.model.OrderFood

@Keep
data class MenuItemResponse(
    @SerializedName("alamat_resto")
    val restaurantAddress: String?,
    @SerializedName("detail")
    val description: String?,
    @SerializedName("harga")
    val price: Int?,
    @SerializedName("harga_format")
    val formattedPrice: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("nama")
    val name: String?
)

fun MenuItemResponse.toMenu() = OrderFood(
    foodName = this.name.orEmpty(),
    foodPrice = this.price?: 0,
    imgFood = this.imageUrl.orEmpty(),
    desc = this.description.orEmpty()
)

fun Collection<MenuItemResponse>.toMenuList() = this.map { it.toMenu() }