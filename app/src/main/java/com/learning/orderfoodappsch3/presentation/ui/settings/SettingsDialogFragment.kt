package com.learning.orderfoodappsch3.presentation.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.learning.orderfoodappsch3.data.datastore.UserPreferenceDataSourceImpl
import com.learning.orderfoodappsch3.data.datastore.appDataStore
import com.learning.orderfoodappsch3.databinding.FragmentSettingsDialogBinding
import com.learning.orderfoodappsch3.presentation.ui.main.MainViewModel
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory
import com.learning.orderfoodappsch3.utils.PreferenceDataStoreHelperImpl

class SettingsDialogFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSettingsDialogBinding
    private val viewModel: SettingsViewModel by viewModels {
        val dataStore = this.requireContext().appDataStore
        val dataStoreHelper = PreferenceDataStoreHelperImpl(dataStore)
        val userPreferenceDataSource = UserPreferenceDataSourceImpl(dataStoreHelper)
        GenericViewModelFactory.create(SettingsViewModel(userPreferenceDataSource))
    }

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsDialogBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToogleSwitchAction()
        observeDarkMode()
    }

    private fun setToogleSwitchAction() {
        binding.ivToogle.setOnCheckedChangeListener { _, isUseDarkMode ->
            viewModel.setUserDarkModePref(isUseDarkMode)
        }
    }

    private fun observeDarkMode() {
        mainViewModel.userDarkModeLiveData.observe(this){
            isUseDarkMode -> binding.ivToogle.isChecked = isUseDarkMode
        }
    }
}