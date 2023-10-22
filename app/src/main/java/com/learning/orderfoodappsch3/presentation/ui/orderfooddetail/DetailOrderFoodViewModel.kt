package com.learning.orderfoodappsch3.presentation.ui.orderfooddetail

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.orderfoodappsch3.data.repository.CartRepo
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.utils.ResultWrapper
import kotlinx.coroutines.launch

class DetailOrderFoodViewModel(
    private val extras: Bundle?,
    private val cartRepo: CartRepo
): ViewModel() {
    private val _resultToCart = MutableLiveData<ResultWrapper<Boolean>>()
    val resultToCart: LiveData<ResultWrapper<Boolean>> get() =_resultToCart

    val orderFood = extras?.getParcelable<OrderFood>(DetailOrderFoodActivity.EXTRA_ORDER_FOOD)

    val priceLiveData = MutableLiveData<Int>().apply { postValue(0) }

    val countOrderLiveData = MutableLiveData<Int>().apply { postValue(0) }




    fun toCart() {
        viewModelScope.launch {
            val foodQuantity =
                if ((countOrderLiveData.value?: 0)<= 0) 1 else countOrderLiveData.value ?: 0
            orderFood?.let {
                cartRepo.createCart(it, foodQuantity).collect { result -> _resultToCart.postValue(result)}
            }
        }
    }

    fun plus(){
        val count = (countOrderLiveData.value ?: 0) + 1
        val pricemenu = orderFood?. foodPrice ?: 0
        countOrderLiveData.postValue(count)
        priceLiveData.postValue(count * pricemenu)
    }

    fun minus(){
        if ((countOrderLiveData.value ?: 0) > 0){
            val count = (countOrderLiveData.value ?: 0) - 1
            val pricemenu = orderFood?. foodPrice ?: 0
            countOrderLiveData.postValue(count)
            priceLiveData.postValue(count * pricemenu)
        }
    }
}