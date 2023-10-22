package com.learning.orderfoodappsch3.presentation.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.learning.orderfoodappsch3.R
import com.learning.orderfoodappsch3.data.network.firebase.auth.FirebaseAuthDataSource
import com.learning.orderfoodappsch3.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.learning.orderfoodappsch3.data.repository.UserRepository
import com.learning.orderfoodappsch3.data.repository.UserRepositoryImpl
import com.learning.orderfoodappsch3.databinding.ActivityChangeProfileBinding
import com.learning.orderfoodappsch3.presentation.ui.main.MainActivity
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory
import com.learning.orderfoodappsch3.utils.proceedWhen
import kotlinx.coroutines.launch

class ProfileChangeActivity: AppCompatActivity() {
    private val binding: ActivityChangeProfileBinding by lazy {
        ActivityChangeProfileBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileChangeViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): ProfileChangeViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource: FirebaseAuthDataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo: UserRepository = UserRepositoryImpl(dataSource)
        return ProfileChangeViewModel(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListeners()
        showDataUser()
        setupForm()
        observeData()
    }

    private fun showDataUser() {
        val name = viewModel.getCurrentUser()?.fullName.orEmpty()
        val email = viewModel.getCurrentUser()?.email.orEmpty()

        binding.editTextName.setText(name)
        binding.editTextEmail.setText(email)
    }

    private fun setClickListeners() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.btnChangeProfile.setOnClickListener {
            requestChangeProfile()
        }
        binding.tvChangePwd.setOnClickListener {
            requestChangePassword()
        }
    }

    private fun setupForm() {
        binding.textInputName.isVisible = true
        binding.textInputEmail.isVisible = true
        binding.textInputEmail.isEnabled = false
    }

    private fun observeData() {
        viewModel.changeProfileResult.observe(this){
            it.proceedWhen(doOnLoading = {
                binding.pbLoading.isVisible = true
                binding.btnChangeProfile.isVisible = false
            }, doOnSuccess = {
                binding.pbLoading.isVisible = false
                binding.btnChangeProfile.isVisible = true
                binding.btnChangeProfile.isEnabled = true
                Toast.makeText(this, "Change profile success!", Toast.LENGTH_SHORT).show()
                navigateToMain()
            }, doOnError = {
                binding.pbLoading.isVisible = false
                binding.btnChangeProfile.isVisible = true
                binding.btnChangeProfile.isEnabled = true
                Toast.makeText(this,
                    "Change Profile Failed: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("open_profile_fragment", true)
        startActivity(intent)
    }

    private fun checkNameValid(): Boolean {
        return if (binding.editTextName.text?.isEmpty() == true){
            binding.textInputName.isErrorEnabled = true
            binding.textInputName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.textInputName.isErrorEnabled = false
            true
        }
    }

    private fun requestChangeProfile() {
        if (checkNameValid()){
            lifecycleScope.launch {
                val fullName = binding.editTextName.text.toString()
                viewModel.changeProfile(fullName)
            }
        }
    }

    private fun requestChangePassword() {
        viewModel.createChangePwdRequest()
        AlertDialog.Builder(this)
            .setMessage(
                getString(R.string.change_password_request_send_to_your_email) +
                        "${viewModel.getCurrentUser()?.email}"
            )
            .setPositiveButton(getString(R.string.okay)) { _, _, ->
                //do nothing
            }.create().show()
    }
}