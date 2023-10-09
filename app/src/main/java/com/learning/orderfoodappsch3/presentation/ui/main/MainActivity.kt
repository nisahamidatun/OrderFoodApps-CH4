package com.learning.orderfoodappsch3.presentation.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.learning.orderfoodappsch3.R
import com.learning.orderfoodappsch3.data.datastore.UserPreferenceDataSourceImpl
import com.learning.orderfoodappsch3.data.datastore.appDataStore
import com.learning.orderfoodappsch3.databinding.ActivityMainBinding
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory
import com.learning.orderfoodappsch3.utils.PreferenceDataStoreHelperImpl

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels {
        val dataStore = this.appDataStore
        val dataStoreHelper = PreferenceDataStoreHelperImpl(dataStore)
        val userPreferenceDataSource = UserPreferenceDataSourceImpl(dataStoreHelper)
        GenericViewModelFactory.create(MainViewModel(userPreferenceDataSource))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBottomNav()
        observeDarkModePref()
    }

    private fun setBottomNav() {
        val bottomNavController = findNavController(R.id.nav_host_fragment)
        binding.btmNav.setupWithNavController(bottomNavController)
    }

    private fun observeDarkModePref() {
        viewModel.userDarkModeLiveData.observe(this) {
            isUseDarkMode -> AppCompatDelegate.setDefaultNightMode(
                if (isUseDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}