package com.learning.orderfoodappsch3.presentation.ui.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.learning.orderfoodappsch3.R
import com.learning.orderfoodappsch3.data.database.AppDatabase
import com.learning.orderfoodappsch3.data.database.datasource.CartDataSource
import com.learning.orderfoodappsch3.data.database.datasource.CartDataSourceImpl
import com.learning.orderfoodappsch3.data.network.api.datasource.RestaurantApiDataSource
import com.learning.orderfoodappsch3.data.network.api.service.RestaurantService
import com.learning.orderfoodappsch3.data.repository.CartRepo
import com.learning.orderfoodappsch3.data.repository.CartRepoImpl
import com.learning.orderfoodappsch3.databinding.ActivityCheckoutBinding
import com.learning.orderfoodappsch3.presentation.common.adapter.CartListAdapter
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory
import com.learning.orderfoodappsch3.utils.proceedWhen
import com.learning.orderfoodappsch3.utils.toCurrencyFormat

class CheckoutActivity : AppCompatActivity() {
    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeData()
        setupList()
        setClickListener()
    }

    private val viewModel: CheckoutViewModel by viewModels {
        val db = AppDatabase.getInstance(this)
        val cDao = db.cartDao()
        val sdc: CartDataSource = CartDataSourceImpl(cDao)
        val chucker = ChuckerInterceptor(this)
        val service = RestaurantService.invoke(chucker)
        val apiDataSource = RestaurantApiDataSource(service)
        val repo: CartRepo = CartRepoImpl(sdc, apiDataSource)
        GenericViewModelFactory.create(CheckoutViewModel(repo))
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnCheckout.setOnClickListener {
            viewModel.order()
        }
    }

    private fun setupList() {
        binding.layoutContent.rvCart.adapter = adapter
    }

    private fun observeData() {
        viewModel.cartList.observe(this) {
            it.proceedWhen(doOnLoading = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.tvError.isVisible = false
                binding.layoutState.pbLoading.isVisible = true
                binding.cvSectionOrder.isVisible = false
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
            }, doOnEmpty = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.text = getString(R.string.empty_cart)
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            }, doOnError = { error ->
                binding.layoutState.root.isVisible = true
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = error.exception?.message.orEmpty()
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutContent.root.isVisible = false
                binding.layoutContent.rvCart.isVisible = false
                binding.cvSectionOrder.isVisible = false
            }, doOnSuccess = { success ->
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = true
                binding.layoutContent.rvCart.isVisible = true
                binding.cvSectionOrder.isVisible = true
                success.payload?.let { (cart, priceTotal) ->
                    adapter.submitData(cart)
                    binding.tvTotalPrice.text = priceTotal.toCurrencyFormat()
                    binding.layoutContent.tvItemPrice.text = priceTotal.toCurrencyFormat()
                }
            })
        }

        viewModel.checkoutResult.observe(this) {
            it.proceedWhen(
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                },
                doOnError = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    Toast.makeText(this, getString(R.string.checkout_failed), Toast.LENGTH_SHORT)
                        .show()
                },
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    dialogSuccessCheckout()
                }
            )
        }
    }

    private fun dialogSuccessCheckout() {
        val dialogView =
            layoutInflater.inflate(R.layout.layout_notification_order_successfully, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        val backButton = dialogView.findViewById<Button>(R.id.btnBackToHome)
        backButton.setOnClickListener {
            alertDialog.dismiss()
            viewModel.clearCart()
            finish()
        }

        alertDialog.show()
    }

}