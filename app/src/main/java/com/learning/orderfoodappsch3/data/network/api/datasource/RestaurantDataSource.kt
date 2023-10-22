package com.learning.orderfoodappsch3.data.network.api.datasource

import com.learning.orderfoodappsch3.data.network.api.model.category.CategoriesResponse
import com.learning.orderfoodappsch3.data.network.api.model.order.OrderRequest
import com.learning.orderfoodappsch3.data.network.api.model.order.OrderResponse
import com.learning.orderfoodappsch3.data.network.api.model.orderfood.MenusResponse
import com.learning.orderfoodappsch3.data.network.api.service.RestaurantService

interface RestaurantDataSource {
    suspend fun getOrderFood(category: String? = null) : MenusResponse
    suspend fun getCategories() : CategoriesResponse
    suspend fun createOrder(orderRequest: OrderRequest) : OrderResponse
}

class RestaurantApiDataSource(private val service: RestaurantService): RestaurantDataSource{
    override suspend fun getOrderFood(category: String?): MenusResponse{
        return service.getMenus(category)
    }

    override suspend fun getCategories(): CategoriesResponse{
        return service.getCategories()
    }

    override suspend fun createOrder(orderRequest: OrderRequest): OrderResponse{
        return service.createOrder(orderRequest)
    }
}