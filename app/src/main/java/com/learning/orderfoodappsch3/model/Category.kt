package com.learning.orderfoodappsch3.model

import java.util.UUID

data class Category (
    val id : String = UUID.randomUUID().toString(),
    val imgCategory: String,
    val nameCategory: String,
)