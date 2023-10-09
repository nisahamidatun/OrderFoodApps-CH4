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
import com.learning.orderfoodappsch3.data.database.AppDatabase
import com.learning.orderfoodappsch3.data.database.datasource.OrderFoodDatabaseDataSource
import com.learning.orderfoodappsch3.data.datastore.UserPreferenceDataSourceImpl
import com.learning.orderfoodappsch3.data.datastore.appDataStore
import com.learning.orderfoodappsch3.data.repository.OrderFoodRepo
import com.learning.orderfoodappsch3.data.repository.OrderFoodRepoImpl
import com.learning.orderfoodappsch3.data.sourcedata.CategoryDataSource
import com.learning.orderfoodappsch3.data.sourcedata.CategoryDataSourceImpl
import com.learning.orderfoodappsch3.databinding.FragmentOrderFoodHomeBinding
import com.learning.orderfoodappsch3.model.OrderFood
import com.learning.orderfoodappsch3.presentation.ui.orderfooddetail.DetailOrderFoodActivity
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.AdapterLayoutMode
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.subadapter.CategoriesAdapter
import com.learning.orderfoodappsch3.presentation.ui.orderfoodhome.adapter.subadapter.OrderFoodAdapter
import com.learning.orderfoodappsch3.presentation.ui.settings.SettingsDialogFragment
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory
import com.learning.orderfoodappsch3.utils.PreferenceDataStoreHelperImpl
import com.learning.orderfoodappsch3.utils.proceedWhen

class OrderFoodHomeFragment() : Fragment() {
    private lateinit var binding: FragmentOrderFoodHomeBinding

    val adapter: OrderFoodAdapter by lazy {
        OrderFoodAdapter( modeAdapterLayout = AdapterLayoutMode.LINEAR,
            onListOrderFoodClicked = {
            navigateToDetail(it)
        })
    }

    private val categorydatasource: CategoryDataSource by lazy {
        CategoryDataSourceImpl()
    }
    private val categoriesAdapter = CategoriesAdapter()

    private val viewModel: OrderFoodHomeViewModel by viewModels {
        val sdc: CategoryDataSource = CategoryDataSourceImpl()
        val db = AppDatabase.getInstance(requireContext())
        val ofDao = db.orderfoodDao()
        val ofDs = OrderFoodDatabaseDataSource(ofDao)
        val repo: OrderFoodRepo = OrderFoodRepoImpl(ofDs, sdc)
        val dStore = this.requireContext().appDataStore
        val dStoreHelper = PreferenceDataStoreHelperImpl(dStore)
        val userPreferenceDataSource = UserPreferenceDataSourceImpl(dStoreHelper)
        GenericViewModelFactory.create(OrderFoodHomeViewModel(repo, userPreferenceDataSource))
    }

    private fun navigateToDetail(item: OrderFood) {
        DetailOrderFoodActivity.startActivity(requireContext(), item)
    }

    private fun openSettingsDialog(){
        SettingsDialogFragment().show(childFragmentManager, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderFoodHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategories()
        setupList()
        setupToggleLayout()
        switchMode()
        fetchData()
    }

    private fun setupCategories() {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategories.adapter = categoriesAdapter
        categoriesAdapter.setItem(categorydatasource.getCategory())
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
        binding.btnSwitchGrid.setOnClickListener { switchToGridLayout() }
        binding.btnSwitchList.setOnClickListener { switchToLinearLayout() }
    }

    private fun switchToGridLayout() {
        viewModel.setListLayoutMenuPref(isLayoutGrid = true)
    }

    private fun switchToLinearLayout() {
        viewModel.setListLayoutMenuPref(isLayoutGrid = false)
    }

    private fun fetchData() {
        viewModel.orderFoodHomeData.observe(viewLifecycleOwner){ item ->
            val span =
                if (adapter.modeAdapterLayout == AdapterLayoutMode.LINEAR) 1 else 2
            item.proceedWhen(doOnLoading = {
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = true
                binding.layoutState.tvError.isVisible = false
                binding.rvFoodMenu.isVisible = false
            }, doOnSuccess = {
                binding.layoutState.root.isVisible = false
                binding.rvFoodMenu.apply {
                    isVisible = true
                    layoutManager = GridLayoutManager(requireContext(), span)
                    adapter = this@OrderFoodHomeFragment.adapter
                }
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                item.payload?.let {
                        data -> adapter.submitData(data)
                }
            }, doOnError = {
                binding.layoutState.root.isVisible = true
                binding.rvFoodMenu.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
            })
        }
    }
}