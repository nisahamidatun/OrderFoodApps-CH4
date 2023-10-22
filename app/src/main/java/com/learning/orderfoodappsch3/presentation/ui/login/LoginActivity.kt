package com.learning.orderfoodappsch3.presentation.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.learning.orderfoodappsch3.R
import com.learning.orderfoodappsch3.data.network.firebase.auth.FirebaseAuthDataSource
import com.learning.orderfoodappsch3.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.learning.orderfoodappsch3.data.repository.UserRepository
import com.learning.orderfoodappsch3.data.repository.UserRepositoryImpl
import com.learning.orderfoodappsch3.presentation.ui.register.RegisterActivity
import com.learning.orderfoodappsch3.databinding.ActivityLoginBinding
import com.learning.orderfoodappsch3.presentation.ui.main.MainActivity
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory
import com.learning.orderfoodappsch3.utils.highLightWord
import com.learning.orderfoodappsch3.utils.proceedWhen

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): LoginViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource: FirebaseAuthDataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo: UserRepository = UserRepositoryImpl(dataSource)
        return LoginViewModel(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListeners()
        observeResult()
    }

    private fun setupForm() {
        binding.layoutLogin.textInputEmail.isVisible = true
        binding.layoutLogin.textInputPassword.isVisible = true
    }

    private fun observeResult() {
        viewModel.loginResult.observe(this) {
            it.proceedWhen(doOnLoading = {
                binding.layoutLogin.pbLoading.isVisible = true
                binding.layoutLogin.cirLoginButton.isVisible = false
            }, doOnSuccess = {
                binding.layoutLogin.pbLoading.isVisible = false
                binding.layoutLogin.cirLoginButton.isVisible = true
                binding.layoutLogin.cirLoginButton.isEnabled = false
                navigateToMain()
            }, doOnError = {
                binding.layoutLogin.pbLoading.isVisible = false
                binding.layoutLogin.cirLoginButton.isVisible = true
                binding.layoutLogin.cirLoginButton.isEnabled = true
                Toast.makeText(this,
                    getString(R.string.login_failed, it.exception?.message), Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun navigateToMain() {
        val intentToMain = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intentToMain)
    }

    private fun setClickListeners() {
        binding.layoutLogin.cirLoginButton.setOnClickListener {
            doLogin()
        }
        binding.layoutLogin.tvNavToRegister.highLightWord(getString(R.string.text_highlight_register)) {
            navigateToRegister()
        }
    }

    private fun navigateToRegister() {
        val intentToRegister = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intentToRegister)
    }

    private fun doLogin() {
        if(isFormValid()){
            val email = binding.layoutLogin.editTextEmail.text.toString().trim()
            val password = binding.layoutLogin.editTextPassword.text.toString().trim()
            viewModel.doLogin(email, password)
        }
    }

    private fun isFormValid(): Boolean {
        val email = binding.layoutLogin.editTextEmail.text.toString().trim()
        val password = binding.layoutLogin.editTextPassword.text.toString().trim()
        return checkEmailValidation(email) && checkPasswordValidation(password, binding.layoutLogin.textInputPassword)
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if(email.isEmpty()){
            binding.layoutLogin.textInputEmail.isErrorEnabled = true
            binding.layoutLogin.textInputEmail.error = getString(R.string.text_error_email_empty)
            false
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.layoutLogin.textInputEmail.isErrorEnabled = true
            binding.layoutLogin.textInputEmail.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.layoutLogin.textInputEmail.isErrorEnabled = false
            true
        }
    }

    private fun checkPasswordValidation(
        password: String,
        textInputLayout: TextInputLayout
    ): Boolean {
        return if(password.isEmpty()){
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = getString(R.string.text_error_password_empty)
            false
        } else if(password.length < 8) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = getString(R.string.text_error_password_less_than_8_char)
            false
        } else {
            textInputLayout.isErrorEnabled = false
            true
        }
    }
}