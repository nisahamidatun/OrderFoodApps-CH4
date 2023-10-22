package com.learning.orderfoodappsch3.presentation.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.orderfoodappsch3.data.repository.UserRepository
import com.learning.orderfoodappsch3.utils.ResultWrapper
import kotlinx.coroutines.launch

class ProfileChangeViewModel(private val repo: UserRepository): ViewModel() {
    private val _changeProfileResult = MutableLiveData<ResultWrapper<Boolean>>()
    val changeProfileResult: LiveData<ResultWrapper<Boolean>>
        get() = _changeProfileResult

    fun getCurrentUser() = repo.getCurrentUser()

    fun createChangePwdRequest(){
        repo.sendChangePasswordRequestByEmail()
    }

    fun changeProfile(fullName: String){
        viewModelScope.launch {
            repo.updateProfile(fullName).collect{result ->
                _changeProfileResult.postValue(result)
            }
        }
    }
}