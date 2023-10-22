package com.learning.orderfoodappsch3.presentation.ui.orderfoodhome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import com.learning.orderfoodappsch3.R
import com.learning.orderfoodappsch3.data.datastore.UserPreferenceDataSourceImpl
import com.learning.orderfoodappsch3.data.datastore.appDataStore
import com.learning.orderfoodappsch3.data.network.api.datasource.RestaurantApiDataSource
import com.learning.orderfoodappsch3.data.network.api.service.RestaurantService
import com.learning.orderfoodappsch3.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.learning.orderfoodappsch3.data.repository.OrderFoodRepositoryImpl
import com.learning.orderfoodappsch3.data.repository.UserRepositoryImpl
import com.learning.orderfoodappsch3.databinding.FragmentOrderFoodHomeBinding
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.presentation.ui.orderfooddetail.DetailOrderFoodActivity
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.AdapterLayoutMode
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.subadapter.CategoriesAdapter
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.subadapter.OrderFoodAdapter
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory
import com.learning.orderfoodappsch3.utils.PreferenceDataStoreHelperImpl
import com.learning.orderfoodappsch3.utils.proceedWhen

class OrderFoodHomeFragment : Fragment() {
    private lateinit var binding: FragmentOrderFoodHomeBinding

    private val adapter: OrderFoodAdapter by lazy {
        OrderFoodAdapter( modeAdapterLayout = AdapterLayoutMode.LINEAR,
            onListOrderFoodClicked = {
            navigateToDetail(it)
        })
    }

    private val categoriesAdapter: CategoriesAdapter by lazy {
        CategoriesAdapter{
            viewModel.getOrderFoods(it.nameCategory.lowercase())
        }
    }

    private val viewModel: OrderFoodHomeViewModel by viewModels {
        val chucker = ChuckerInterceptor(requireContext())
        val service = RestaurantService.invoke(chucker)
        val restoDataSource = RestaurantApiDataSource(service)
        val repo = OrderFoodRepositoryImpl(restoDataSource)
        val dataStore = this.requireContext().appDataStore
        val dataStoreHelper = PreferenceDataStoreHelperImpl(dataStore)
        val userPrefDataSource = UserPreferenceDataSourceImpl(dataStoreHelper)
        val firebaseDataSource = FirebaseAuthDataSourceImpl(FirebaseAuth.getInstance())
        val userRepo = UserRepositoryImpl(firebaseDataSource)
        GenericViewModelFactory.create(OrderFoodHomeViewModel(repo, userPrefDataSource, userRepo))
    }

    private fun navigateToDetail(item: OrderFood) {
        DetailOrderFoodActivity.startActivity(requireContext(), item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderFoodHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfileView()
        setupCategories()
        setupList()
        setupToggleLayout()
        switchMode()
        observeData()
        fetchData()
    }

    private fun setupProfileView() {
        viewModel.userDataLiveData?.fullName?.let { name ->
            binding.username.text = name
        } ?: getString(R.string.nisa).let { defaultName ->
            binding.username.text = defaultName
        }
    }

    private fun setupCategories() {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun setupList() {
        val span  = if (adapter.modeAdapterLayout == AdapterLayoutMode.LINEAR) 1 else 2
        binding.rvFoodMenu.apply {
            layoutManager = GridLayoutManager(requireContext(), span)
            adapter = this@OrderFoodHomeFragment.adapter
        }
    }

    private fun setupToggleLayout() {
        viewModel.layoutMenuListLiveData.observe(viewLifecycleOwner) { isLayoutGrid ->
            val gridVisibility = if (isLayoutGrid) View.GONE else View.VISIBLE
            val linearVisibility = if (isLayoutGrid) View.VISIBLE else View.GONE

            binding.btnSwitchGrid.visibility = gridVisibility
            binding.btnSwitchList.visibility = linearVisibility

            val spanCount = if (isLayoutGrid) 2 else 1
            (binding.rvFoodMenu.layoutManager as GridLayoutManager).spanCount = spanCount

            adapter.modeAdapterLayout = if (isLayoutGrid) AdapterLayoutMode.GRID else AdapterLayoutMode.LINEAR
            adapter.refreshList()
        }
    }

    private fun switchMode() {
        val switchLayout: (Boolean) -> Unit = { isGrid ->
            viewModel.setListLayoutMenuPref(isGrid)
        }

        binding.btnSwitchGrid.setOnClickListener { switchLayout(true) }
        binding.btnSwitchList.setOnClickListener { switchLayout(false) }
    }

    private fun observeData() {
        viewModel.orderFoods.observe(viewLifecycleOwner){ item ->
            val span =
                if (adapter.modeAdapterLayout == AdapterLayoutMode.LINEAR) 1 else 2
            item.proceedWhen(doOnLoading = {
                binding.layoutStateOrderFood.root.isVisible = true
                binding.layoutStateOrderFood.pbLoading.isVisible = true
                binding.layoutStateOrderFood.tvError.isVisible = false
                binding.rvFoodMenu.isVisible = false
            }, doOnSuccess = {
                binding.layoutStateOrderFood.root.isVisible = false
                binding.rvFoodMenu.apply {
                    isVisible = true
                    layoutManager = GridLayoutManager(requireContext(), span)
                    adapter = this@OrderFoodHomeFragment.adapter
                }
                binding.layoutStateOrderFood.pbLoading.isVisible = false
                binding.layoutStateOrderFood.tvError.isVisible = false
                item.payload?.let {
                        data -> adapter.submitData(data)
                }
            }, doOnError = {
                binding.layoutStateOrderFood.root.isVisible = true
                binding.rvFoodMenu.isVisible = false
                binding.layoutStateOrderFood.pbLoading.isVisible = false
                binding.layoutStateOrderFood.tvError.isVisible = true
            })
        }

        viewModel.categories.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.rvCategories.isVisible = true
                    binding.layoutStateCategory.tvError.isVisible = false
                    binding.layoutStateCategory.pbLoading.isVisible = false

                    result.payload?.let { category ->
                        categoriesAdapter.setItem(category)
                    }
                },
                doOnLoading = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = true
                    binding.rvCategories.isVisible = false
                },
                doOnError = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutStateCategory.tvError.isVisible = true
                    binding.layoutStateCategory.tvError.text = it.exception?.message.orEmpty()
                    binding.rvCategories.isVisible = false
                }
            )
        }
    }

    private fun fetchData() {
        viewModel.getCategories()
        viewModel.getOrderFoods()
    }
}