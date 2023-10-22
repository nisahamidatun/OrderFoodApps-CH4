package com.learning.orderfoodappsch3.presentation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.orderfoodappsch3.data.repository.UserRepository
import com.learning.orderfoodappsch3.utils.ResultWrapper
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<ResultWrapper<Boolean>>()
    val loginResult : LiveData<ResultWrapper<Boolean>>
        get() = _loginResult

    fun doLogin(email : String, password : String){
        viewModelScope.launch {
            repo.doLogin(email, password).collect{
                _loginResult.postValue(it)
            }
        }
    }
}