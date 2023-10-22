package com.learning.orderfoodappsch3.data.repository

import com.learning.orderfoodappsch3.data.network.api.datasource.RestaurantDataSource
import com.learning.orderfoodappsch3.data.network.api.model.category.toCategoryList
import com.learning.orderfoodappsch3.data.network.api.model.orderfood.toMenuList
import com.learning.orderfoodappsch3.model.Category
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.utils.ResultWrapper
import com.learning.orderfoodappsch3.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface OrderFoodRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
    fun getOrderFoods(category: String? = null): Flow<ResultWrapper<List<OrderFood>>>
}

class OrderFoodRepositoryImpl(
    private val apiDataSource: RestaurantDataSource,
) : OrderFoodRepository {

    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            val apiResult = apiDataSource.getCategories()
            apiResult.data?.toCategoryList() ?: emptyList()
        }
    }

    override fun getOrderFoods(category: String?): Flow<ResultWrapper<List<OrderFood>>> {
        return proceedFlow {
            apiDataSource.getOrderFood(category).data?.toMenuList() ?: emptyList()
        }
    }
}
