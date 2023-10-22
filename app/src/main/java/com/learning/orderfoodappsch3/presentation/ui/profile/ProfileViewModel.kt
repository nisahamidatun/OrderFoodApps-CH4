package com.learning.orderfoodappsch3.presentation.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.learning.orderfoodappsch3.data.repository.UserRepository
import com.learning.orderfoodappsch3.model.User

class ProfileViewModel(private val repo: UserRepository) : ViewModel() {
    private val _profileLiveData = MutableLiveData<User?>()
    val profileLiveData: LiveData<User?>
        get() = _profileLiveData

    fun getCurrentUser() {
        val updateResult = repo.getCurrentUser()
        _profileLiveData.value = updateResult
    }

    fun doLogout(){
        repo.doLogout()
    }
}