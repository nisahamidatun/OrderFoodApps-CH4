package com.learning.orderfoodappsch3.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.appDataStore by preferencesDataStore(
    name = "OrderFoodDataStore"
)

