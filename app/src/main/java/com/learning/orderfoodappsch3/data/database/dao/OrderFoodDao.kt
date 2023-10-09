package com.learning.orderfoodappsch3.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.learning.orderfoodappsch3.data.database.entity.OrderFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderFoodDao {
    @Query("SELECT * FROM ORDERFOOD")
    fun getAllOrderFood(): Flow<List<OrderFoodEntity>>

    @Query("SELECT * FROM ORDERFOOD WHERE id == :id")
    fun getOrderFoodbyId(id: Int): Flow<OrderFoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderFood(orderfood: List<OrderFoodEntity>)

    @Update
    suspend fun updateOrderFood(orderfood: OrderFoodEntity) : Int

    @Delete
    suspend fun deleteOrderFood(orderfood: OrderFoodEntity) : Int

}