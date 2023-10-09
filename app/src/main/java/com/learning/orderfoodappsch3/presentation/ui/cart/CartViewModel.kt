package com.learning.orderfoodappsch3.presentation.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.learning.orderfoodappsch3.data.repository.CartRepo
import com.learning.orderfoodappsch3.model.Cart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(private val repo: CartRepo): ViewModel() {
    val cartList = repo.getDataCartFromUser().asLiveData(Dispatchers.IO)

    fun incCart(item: Cart){
        viewModelScope.launch { repo.increaseCart(item).collect() }
    }
    fun decCart(item: Cart){
        viewModelScope.launch { repo.decreaseCart(item).collect() }
    }
    fun delCart(item: Cart){
        viewModelScope.launch { repo.deleteCart(item).collect() }
    }
    fun setNote(item: Cart){
        viewModelScope.launch { repo.setNote(item).collect() }
    }

}