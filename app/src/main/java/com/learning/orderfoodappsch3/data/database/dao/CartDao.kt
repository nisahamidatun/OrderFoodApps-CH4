package com.learning.orderfoodappsch3.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.learning.orderfoodappsch3.data.database.entity.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM CART")
    fun getAllCart(): Flow<List<CartEntity>>

    @Query("SELECT * FROM CART WHERE id == :cartId")
    fun getCartById(cartId: Int): Flow<CartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChart(chart: List<CartEntity>)

    @Update
    suspend fun updateCart(cart: CartEntity): Int

    @Delete
    suspend fun deleteCart(cart: CartEntity): Int

    @Query("DELETE FROM CART")
    suspend fun deleteAll(): Int
}