package com.learning.orderfoodappsch3.data.network.api.model.category

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.learning.orderfoodappsch3.model.Category

@Keep
data class CategoryResponse(
    @SerializedName("image_url")
    val imgUrl: String?,
    @SerializedName("nama")
    val name: String?,
)

fun CategoryResponse.toCategory() = Category(
    imgCategory = this.imgUrl.orEmpty(),
    nameCategory = this.name.orEmpty(),
)

fun Collection<CategoryResponse>.toCategoryList() = this.map {
    it.toCategory()
}