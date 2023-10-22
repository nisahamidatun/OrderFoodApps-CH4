package com.learning.orderfoodappsch3.data.network.api.service

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.learning.orderfoodappsch3.BuildConfig
import com.learning.orderfoodappsch3.data.network.api.model.category.CategoriesResponse
import com.learning.orderfoodappsch3.data.network.api.model.order.OrderRequest
import com.learning.orderfoodappsch3.data.network.api.model.order.OrderResponse
import com.learning.orderfoodappsch3.data.network.api.model.orderfood.MenusResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RestaurantService {
    @GET("listmenu")
    suspend fun getMenus(@Query("c") category: String? = null): MenusResponse

    @GET("category")
    suspend fun getCategories(): CategoriesResponse

    @POST("order")
    suspend fun createOrder(@Body orderRequest: OrderRequest): OrderResponse

    companion object {
        @JvmStatic
        operator fun invoke(chucker : ChuckerInterceptor): RestaurantService {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(chucker)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(RestaurantService::class.java)
        }
    }
}