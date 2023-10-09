package com.learning.orderfoodappsch3.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.learning.orderfoodappsch3.data.datastore.UserPreferenceDataSource
import kotlinx.coroutines.Dispatchers

class MainViewModel(private val userPreferenceDataSource: UserPreferenceDataSource): ViewModel() {
    val userDarkModeLiveData = userPreferenceDataSource.getUserDarkModePrefFlow().asLiveData(Dispatchers.IO)
}