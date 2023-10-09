package com.learning.orderfoodappsch3.presentation.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.orderfoodappsch3.data.datastore.UserPreferenceDataSource
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreferenceDataSource: UserPreferenceDataSource): ViewModel() {
    fun setUserDarkModePref(isUsingDarkMode: Boolean) {
        viewModelScope.launch {
            userPreferenceDataSource.setUserDarkModePref(isUsingDarkMode)
        }
    }
}