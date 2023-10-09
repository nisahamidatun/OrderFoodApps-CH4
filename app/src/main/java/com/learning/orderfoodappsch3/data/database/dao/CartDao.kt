package com.learning.orderfoodappsch3.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import com.learning.orderfoodappsch3.data.database.relation.CartOrderFoodRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM CART")
    fun getAllCart(): Flow<List<CartOrderFoodRelation>>

    @Query("SELECT * FROM CART WHERE id == :cartId")
    fun getCartById(cartId: Int): Flow<CartOrderFoodRelation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChart(orderfood: List<CartEntity>)

    @Update
    suspend fun updateCart(cart: CartEntity): Int

    @Delete
    suspend fun deleteCart(cart: CartEntity): Int
}