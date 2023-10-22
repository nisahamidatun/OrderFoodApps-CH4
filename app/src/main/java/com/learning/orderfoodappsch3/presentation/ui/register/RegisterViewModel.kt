package com.learning.orderfoodappsch3.presentation.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.orderfoodappsch3.data.repository.UserRepository
import com.learning.orderfoodappsch3.utils.ResultWrapper
import kotlinx.coroutines.launch

class RegisterViewModel(private val repo: UserRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<ResultWrapper<Boolean>>()
    val registerResult: LiveData<ResultWrapper<Boolean>>
        get() = _registerResult

    fun doRegister(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            repo.doRegister(fullName, email, password).collect{
                _registerResult.postValue(it)
            }
        }
    }
}