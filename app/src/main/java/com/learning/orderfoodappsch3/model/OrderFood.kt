package com.learning.orderfoodappsch3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderFood(
    val id : Int? = null,
    val imgFood  : String,
    val foodName : String,
    val desc : String,
    val foodPrice : Double
) : Parcelable