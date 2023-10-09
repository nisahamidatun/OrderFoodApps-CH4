package com.learning.orderfoodappsch3.data.database.datasource

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.learning.orderfoodappsch3.data.database.dao.OrderFoodDao
import com.learning.orderfoodappsch3.data.database.entity.OrderFoodEntity
import kotlinx.coroutines.flow.Flow

interface OrderFoodDataSource {
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

class OrderFoodDatabaseDataSource (private val dao: OrderFoodDao) : OrderFoodDataSource {
    override fun getAllOrderFood(): Flow<List<OrderFoodEntity>> {
        return dao.getAllOrderFood()
    }

    override fun getOrderFoodbyId(id: Int): Flow<OrderFoodEntity> {
        return dao.getOrderFoodbyId(id)
    }

    override suspend fun insertOrderFood(orderfood: List<OrderFoodEntity>) {
        return dao.insertOrderFood(orderfood)
    }

    override suspend fun updateOrderFood(orderfood: OrderFoodEntity): Int {
        return dao.updateOrderFood(orderfood)
    }

    override suspend fun deleteOrderFood(orderfood: OrderFoodEntity): Int {
        return dao.deleteOrderFood(orderfood)
    }
}