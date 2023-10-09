package com.learning.orderfoodappsch3.data.database.datasource

import com.learning.orderfoodappsch3.data.database.dao.CartDao
import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import com.learning.orderfoodappsch3.data.database.relation.CartOrderFoodRelation
import kotlinx.coroutines.flow.Flow

interface CartDataSource{
    fun getAllCart(): Flow<List<CartOrderFoodRelation>>
    fun getCartById(cartId: Int): Flow<CartOrderFoodRelation>
    suspend fun insertCart(cart: CartEntity): Long
    suspend fun insertChart(cart: List<CartEntity>)
    suspend fun updateCart(cart: CartEntity): Int
    suspend fun deleteCart(cart: CartEntity): Int
}

class CartDatabaseDataSource(private val cartDao: CartDao): CartDataSource{
    override fun getAllCart(): Flow<List<CartOrderFoodRelation>> {
        return cartDao.getAllCart()
    }

    override fun getCartById(cartId: Int): Flow<CartOrderFoodRelation> {
        return cartDao.getCartById(cartId)
    }

    override suspend fun insertCart(cart: CartEntity): Long {
        return cartDao.insertCart(cart)
    }

    override suspend fun insertChart(cart: List<CartEntity>) {
        return cartDao.insertChart(cart)
    }

    override suspend fun updateCart(cart: CartEntity): Int {
        return cartDao.updateCart(cart)
    }

    override suspend fun deleteCart(cart: CartEntity): Int {
        return cartDao.deleteCart(cart)
    }
}