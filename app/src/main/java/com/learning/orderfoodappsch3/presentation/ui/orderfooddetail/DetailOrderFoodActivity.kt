package com.learning.orderfoodappsch3.presentation.ui.orderfooddetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import coil.load
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.learning.orderfoodappsch3.R
import com.learning.orderfoodappsch3.data.database.AppDatabase
import com.learning.orderfoodappsch3.data.database.datasource.CartDataSource
import com.learning.orderfoodappsch3.data.database.datasource.CartDataSourceImpl
import com.learning.orderfoodappsch3.data.network.api.datasource.RestaurantApiDataSource
import com.learning.orderfoodappsch3.data.network.api.service.RestaurantService
import com.learning.orderfoodappsch3.data.repository.CartRepo
import com.learning.orderfoodappsch3.data.repository.CartRepoImpl
import com.learning.orderfoodappsch3.databinding.ActivityDetailOrderFoodBinding
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory
import com.learning.orderfoodappsch3.utils.proceedWhen
import com.learning.orderfoodappsch3.utils.toCurrencyFormat

class DetailOrderFoodActivity : AppCompatActivity() {
    private val binding: ActivityDetailOrderFoodBinding by lazy {
        ActivityDetailOrderFoodBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailOrderFoodViewModel by viewModels {
        val db = AppDatabase.getInstance(this)
        val cDao = db.cartDao()
        val dsc: CartDataSource = CartDataSourceImpl(cDao)
        val chucker = ChuckerInterceptor(this)
        val service = RestaurantService.invoke(chucker)
        val apiDataSource = RestaurantApiDataSource(service)
        val repo: CartRepo = CartRepoImpl(dsc, apiDataSource)
        GenericViewModelFactory.create(
            DetailOrderFoodViewModel(intent?.extras, repo)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindOrderFood(viewModel.orderFood)
        observeData()
        setClickListener()
    }

    private fun bindOrderFood(orderFood: OrderFood?){
        orderFood?.let {
            item ->
            binding.tvOrderFoodName.text = item.foodName
            binding.tvDescMenu.text = item.desc
            binding.ivOrderFood.load(item.imgFood) { crossfade(true)}
            binding.tvPriceFood.text = item.foodPrice.toCurrencyFormat()
        }
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this){
            binding.tvTotalPrice.text = it.toCurrencyFormat()
        }
        viewModel.countOrderLiveData.observe(this) {
            binding.tvTotalOrder.text = it.toString()
        }
        viewModel.resultToCart.observe(this){
            it.proceedWhen(doOnSuccess = {
                Toast.makeText(this,
                    getString(R.string.your_food_has_added_to_cart), Toast.LENGTH_SHORT).show()
                finish()
            }, doOnError = {
                Toast.makeText(this,
                    getString(R.string.your_food_failed_to_add_to_cart), Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun setClickListener() {
        binding.btnAdd.setOnClickListener{
            viewModel.plus()
        }
        binding.btnMinus.setOnClickListener{
            viewModel.minus()
        }
        binding.cvBtnAddToCart.setOnClickListener{
            viewModel.toCart()
        }
        binding.ivBackButton.setOnClickListener{
            onBackPressed()
        }
        binding.tvMaps.setOnClickListener{
            navigateToGoogleMaps(viewModel.orderFood)
        }
    }

    private fun navigateToGoogleMaps(orderFood: OrderFood?) {
        orderFood?.let {
            val mapUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_ORDER_FOOD = "extra_order_food"
        fun startActivity(context: Context, orderfood: OrderFood) {
            val intent = Intent(context, DetailOrderFoodActivity::class.java)
            intent.putExtra(EXTRA_ORDER_FOOD, orderfood)
            context.startActivity(intent)
        }
    }
}