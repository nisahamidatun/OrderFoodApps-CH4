package com.learning.orderfoodappsch3.presentation.ui.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.learning.orderfoodappsch3.R
import com.learning.orderfoodappsch3.data.database.AppDatabase
import com.learning.orderfoodappsch3.data.database.datasource.CartDataSource
import com.learning.orderfoodappsch3.data.database.datasource.CartDataSourceImpl
import com.learning.orderfoodappsch3.data.network.api.datasource.RestaurantApiDataSource
import com.learning.orderfoodappsch3.data.network.api.service.RestaurantService
import com.learning.orderfoodappsch3.data.repository.CartRepo
import com.learning.orderfoodappsch3.data.repository.CartRepoImpl
import com.learning.orderfoodappsch3.databinding.FragmentCartBinding
import com.learning.orderfoodappsch3.model.Cart
import com.learning.orderfoodappsch3.presentation.common.adapter.CartListAdapter
import com.learning.orderfoodappsch3.presentation.common.adapter.CartListener
import com.learning.orderfoodappsch3.presentation.ui.checkout.CheckoutActivity
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory
import com.learning.orderfoodappsch3.utils.hideKeyboard
import com.learning.orderfoodappsch3.utils.proceedWhen
import com.learning.orderfoodappsch3.utils.toCurrencyFormat

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels{
        val db = AppDatabase.getInstance(requireContext())
        val cDao = db.cartDao()
        val cartDataSource: CartDataSource = CartDataSourceImpl(cDao)
        val chucker = ChuckerInterceptor(requireContext())
        val service = RestaurantService.invoke(chucker)
        val apiDataSource = RestaurantApiDataSource(service)
        val repo: CartRepo = CartRepoImpl(cartDataSource, apiDataSource)
        GenericViewModelFactory.create(CartViewModel(repo))
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter(object : CartListener{
            override fun onIncItemCartClicked(cart: Cart) {
                viewModel.incCart(cart)
            }

            override fun onDecItemCartClicked(cart: Cart) {
                viewModel.decCart(cart)
            }

            override fun onDelItemCartClicked(cart: Cart) {
                viewModel.delCart(cart)
            }

            override fun onDoneEditNotes(cart: Cart) {
                viewModel.setNote(cart)
                hideKeyboard()
            }
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setupList()
        onClickListener()
    }

    private fun observeData() {
        viewModel.cartList.observe(viewLifecycleOwner){
            it.proceedWhen( doOnLoading = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = true
                binding.layoutState.tvError.isVisible = false
                binding.rvCart.isVisible = false
            }, doOnEmpty = {
                data ->
                binding.layoutState.root.isVisible = true
                binding.rvCart.isVisible = false
                data.payload?.let { (_, priceTotal) ->
                    binding.tvTotalPriceOrder.text = priceTotal.toCurrencyFormat()
                }
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = getString(R.string.empty_cart)
                binding.layoutState.pbLoading.isVisible = false
            }, doOnError = {
                error ->
                binding.layoutState.root.isVisible = true
                binding.rvCart.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = error.exception?.message.orEmpty()
            }, doOnSuccess = {
                success ->
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.rvCart.isVisible = true
                success.payload?.let { (cart, priceTotal) ->
                    adapter.submitData(cart)
                    binding.tvTotalPriceOrder.text = priceTotal.toCurrencyFormat()
                }
            })
        }
    }

    private fun setupList(){
        binding.rvCart.adapter = adapter
    }

    private fun onClickListener() {
        binding.btnCheckout.setOnClickListener{
            context?.startActivity(Intent(requireContext(), CheckoutActivity::class.java))
        }
    }

}