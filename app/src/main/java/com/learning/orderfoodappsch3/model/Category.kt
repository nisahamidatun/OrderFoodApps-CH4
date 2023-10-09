package com.learning.orderfoodappsch3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Category (
    val id : String = UUID.randomUUID().toString(),
    val imgCategory: String,
    val nameCategory: String
) : Parcelable