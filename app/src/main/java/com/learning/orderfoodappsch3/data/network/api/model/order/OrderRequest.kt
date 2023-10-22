package com.learning.orderfoodappsch3.data.network.api.model.order

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class OrderRequest(
    @SerializedName("username")
    val username: String?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("orders")
    val orders: List<OrderItemRequest>?
)