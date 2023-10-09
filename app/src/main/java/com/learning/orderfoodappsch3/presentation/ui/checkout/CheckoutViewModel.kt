package com.learning.orderfoodappsch3.presentation.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.learning.orderfoodappsch3.data.repository.CartRepo
import kotlinx.coroutines.Dispatchers

class CheckoutViewModel(private val cartRepo: CartRepo): ViewModel() {
    var cartList = cartRepo.getDataCartFromUser().asLiveData(Dispatchers.IO)
}