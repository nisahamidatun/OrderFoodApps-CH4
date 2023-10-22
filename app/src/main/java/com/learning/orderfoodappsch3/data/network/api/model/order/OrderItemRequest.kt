package com.learning.orderfoodappsch3.data.network.api.model.order

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderItemRequest(
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Int,
    @SerializedName("notes")
    val notes: String?,
    @SerializedName("qty")
    val qty: Int?
)