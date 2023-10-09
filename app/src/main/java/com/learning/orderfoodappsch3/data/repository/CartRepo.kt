package com.learning.orderfoodappsch3.data.repository

import com.learning.orderfoodappsch3.data.database.datasource.CartDataSource
import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import com.learning.orderfoodappsch3.data.database.mapper.toCartEntity
import com.learning.orderfoodappsch3.data.database.mapper.toCartOrderFoodList
import com.learning.orderfoodappsch3.model.Cart
import com.learning.orderfoodappsch3.model.CartOrderFood
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.utils.ResultWrapper
import com.learning.orderfoodappsch3.utils.proceed
import com.learning.orderfoodappsch3.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface CartRepo {
    fun getDataCartFromUser(): Flow<ResultWrapper<Pair<List<CartOrderFood>, Double>>>
    suspend fun createCart(orderFood: OrderFood, quantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setNote(item: Cart): Flow<ResultWrapper<Boolean>>
}

class CartRepoImpl(private val dataSource: CartDataSource): CartRepo{
    override fun getDataCartFromUser(): Flow<ResultWrapper<Pair<List<CartOrderFood>, Double>>> {
        return dataSource.getAllCart().map {
            proceed {
                val result = it.toCartOrderFoodList()
                val priceTotal = result.sumOf {
                    val pricePerItem = it.orderFood.foodPrice
                    val quantity = it.cart.quantityCartItem
                    pricePerItem * quantity
                }
                Pair(result, priceTotal)
            }
        }
            .map {
                if (it.payload?.first?.isEmpty() == true)
                    ResultWrapper.Empty(it.payload)
                else it
            }
            .onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override suspend fun createCart(
        orderFood: OrderFood,
        quantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return orderFood.id?.let {
            orderfoodId ->
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(orderfoodId = orderfoodId, quantityCartItem = quantity)
                )
                affectedRow > 0
            }
        } ?: flow { emit(ResultWrapper.Error(IllegalStateException("Your Food ID option was not found"))) }
    }

    override suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply { quantityCartItem += 1 }
        return proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart = item.copy().apply { quantityCartItem -= 1 }
        return if (modifiedCart.quantityCartItem <= 0){
            proceedFlow { dataSource.deleteCart(modifiedCart.toCartEntity()) > 0 }
        } else {
            proceedFlow { dataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override suspend fun setNote(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateCart(item.toCartEntity()) > 0 }
    }
}