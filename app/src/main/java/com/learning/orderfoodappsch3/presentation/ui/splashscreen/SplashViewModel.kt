package com.learning.orderfoodappsch3.presentation.ui.splashscreen

import androidx.lifecycle.ViewModel
import com.learning.orderfoodappsch3.data.repository.UserRepository

class SplashViewModel(private val repo: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = repo.isLoggedIn()
}