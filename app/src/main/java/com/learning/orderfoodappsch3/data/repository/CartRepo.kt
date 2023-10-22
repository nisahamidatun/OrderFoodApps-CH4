package com.learning.orderfoodappsch3.data.repository

import com.learning.orderfoodappsch3.data.database.datasource.CartDataSource
import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import com.learning.orderfoodappsch3.data.database.mapper.toCartEntity
import com.learning.orderfoodappsch3.data.database.mapper.toCartList
import com.learning.orderfoodappsch3.data.network.api.datasource.RestaurantApiDataSource
import com.learning.orderfoodappsch3.data.network.api.model.order.OrderItemRequest
import com.learning.orderfoodappsch3.data.network.api.model.order.OrderRequest
import com.learning.orderfoodappsch3.model.Cart
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.utils.ResultWrapper
import com.learning.orderfoodappsch3.utils.proceed
import com.learning.orderfoodappsch3.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

interface CartRepo {
    fun getDataCartFromUser(): Flow<ResultWrapper<Pair<List<Cart>, Int>>>
    suspend fun createCart(orderFood: OrderFood, quantity: Int): Flow<ResultWrapper<Boolean>>
    suspend fun increaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun decreaseCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun setNote(item: Cart): Flow<ResultWrapper<Boolean>>
    suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>>
    suspend fun deleteAll()
}

class CartRepoImpl(
    private val dataSource: CartDataSource,
    private val restaurantApiDataSource: RestaurantApiDataSource
): CartRepo{

    override suspend fun deleteAll() {
        dataSource.deleteAll()
    }

    override fun getDataCartFromUser(): Flow<ResultWrapper<Pair<List<Cart>, Int>>> {
        return dataSource.getAllCart().map {
            proceed {
                val result = it.toCartList()
                val priceTotal = result.sumOf {
                    val pricePerItem = it.orderfoodPrice
                    val quantity = it.quantityCartItem
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
            }
    }

    override suspend fun createCart(
        orderFood: OrderFood,
        quantity: Int
    ): Flow<ResultWrapper<Boolean>> {
        return orderFood.foodName.let {
            proceedFlow {
                val affectedRow = dataSource.insertCart(
                    CartEntity(
                        quantityCartItem = quantity,
                        orderfoodImgUrl = orderFood.imgFood,
                        orderfoodName = orderFood.foodName,
                        orderfoodPrice = orderFood.foodPrice
                    )
                )
                affectedRow > 0
            }
        }
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

    override suspend fun order(items: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val orderItems = items.map {
                OrderItemRequest(it.orderfoodName, it.orderfoodPrice, it.notes, it.quantityCartItem)
            }
            val orderRequest = OrderRequest(
                username = "username",
                total = orderItems.map { it.qty?.times((it.price ?: 0)) ?: 0 }.sum(),
                orders = orderItems
            )
            restaurantApiDataSource.createOrder(orderRequest).status == true
        }
    }
}