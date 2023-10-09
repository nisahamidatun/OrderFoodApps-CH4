package com.learning.orderfoodappsch3.data.repository

import com.learning.orderfoodappsch3.data.database.datasource.OrderFoodDataSource
import com.learning.orderfoodappsch3.data.database.mapper.toOrderFoodList
import com.learning.orderfoodappsch3.data.sourcedata.CategoryDataSource
import com.learning.orderfoodappsch3.model.Category
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.utils.ResultWrapper
import com.learning.orderfoodappsch3.utils.proceed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class OrderFoodRepoImpl(
    private val orderFoodDataSource: OrderFoodDataSource,
    private val categoryDataSource: CategoryDataSource,
):OrderFoodRepo {
    override fun getOrderFood():Flow<ResultWrapper<List<OrderFood>>> {
        return orderFoodDataSource.getAllOrderFood().map { proceed {it.toOrderFoodList()} }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }

    override fun getCategories(): List<Category>{
        return categoryDataSource.getCategory()
    }
}

interface OrderFoodRepo {
    fun getOrderFood(): Flow<ResultWrapper<List<OrderFood>>>
    fun getCategories(): List<Category>
}
