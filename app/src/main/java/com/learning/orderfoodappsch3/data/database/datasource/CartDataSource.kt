package com.learning.orderfoodappsch3.data.database.datasource

import com.learning.orderfoodappsch3.data.database.dao.CartDao
import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import kotlinx.coroutines.flow.Flow

interface CartDataSource{
    fun getAllCart(): Flow<List<CartEntity>>
    fun getCartById(cartId: Int): Flow<CartEntity>
    suspend fun insertCart(cart: CartEntity): Long
    suspend fun insertChart(chart: List<CartEntity>)
    suspend fun updateCart(cart: CartEntity): Int
    suspend fun deleteCart(cart: CartEntity): Int
    suspend fun deleteAll():Int
}

class CartDataSourceImpl(private val cartDao: CartDao): CartDataSource{
    override fun getAllCart(): Flow<List<CartEntity>> {
        return cartDao.getAllCart()
    }

    override fun getCartById(cartId: Int): Flow<CartEntity> {
        return cartDao.getCartById(cartId)
    }

    override suspend fun insertCart(cart: CartEntity): Long {
        return cartDao.insertCart(cart)
    }

    override suspend fun insertChart(chart: List<CartEntity>) {
        cartDao.insertChart(chart)
    }

    override suspend fun updateCart(cart: CartEntity): Int {
        return cartDao.updateCart(cart)
    }

    override suspend fun deleteCart(cart: CartEntity): Int {
        return cartDao.deleteCart(cart)
    }

    override suspend fun deleteAll(): Int {
        return cartDao.deleteAll()
    }
}
