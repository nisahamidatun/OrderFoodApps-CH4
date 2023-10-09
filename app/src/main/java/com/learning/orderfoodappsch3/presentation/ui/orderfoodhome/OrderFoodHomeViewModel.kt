package com.learning.orderfoodappsch3.presentation.ui.orderfoodhome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learning.orderfoodappsch3.data.datastore.UserPreferenceDataSource
import com.learning.orderfoodappsch3.data.repository.OrderFoodRepo
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderFoodHomeViewModel(
    private val repo: OrderFoodRepo,
    private val userPreferenceDataSource: UserPreferenceDataSource): ViewModel() {
    val orderFoodHomeData: LiveData<ResultWrapper<List<OrderFood>>>
        get() = repo.getOrderFood().asLiveData(Dispatchers.IO)

    val layoutMenuListLiveData = userPreferenceDataSource.getListLayoutMenuPrefFlow().asLiveData(Dispatchers.Main)

    fun setListLayoutMenuPref(isLayoutGrid: Boolean) {
        viewModelScope.launch {
            userPreferenceDataSource.setListLayoutMenuPref(isLayoutGrid)
        }
    }
}