package com.learning.orderfoodappsch3.presentation.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.learning.orderfoodappsch3.R
import com.learning.orderfoodappsch3.data.network.firebase.auth.FirebaseAuthDataSource
import com.learning.orderfoodappsch3.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.learning.orderfoodappsch3.data.repository.UserRepository
import com.learning.orderfoodappsch3.data.repository.UserRepositoryImpl
import com.learning.orderfoodappsch3.databinding.FragmentProfileBinding
import com.learning.orderfoodappsch3.presentation.ui.login.LoginActivity
import com.learning.orderfoodappsch3.utils.GenericViewModelFactory

class ProfileFragment : Fragment() {
    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): ProfileViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource: FirebaseAuthDataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo: UserRepository = UserRepositoryImpl(dataSource)
        return ProfileViewModel(repo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        setupForm()
        setClickListeners()
        showDataUser()
    }

    private fun setupForm() {
        binding.layoutProfile.tvName.isVisible = true
        binding.layoutProfile.tvEmail.isVisible = true
    }

    private fun showDataUser() {
        viewModel.profileLiveData.observe(viewLifecycleOwner) {
            val name = it?.fullName.orEmpty()
            val email = it?.email.orEmpty()

            binding.layoutProfile.tvName.text = name
            binding.layoutProfile.tvEmail.text = email
        }
    }

    private fun setClickListeners() {
        binding.icPencil.setOnClickListener {
            navigateToProfileChange()
        }
        binding.layoutProfile.tvLogout.setOnClickListener {
            doLogout()
        }
    }

    private fun doLogout() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.do_you_want_to_logout))
            .setPositiveButton(getString(R.string.okay)) {_,_ ->
                viewModel.doLogout()
                navigateToLogin()
            }.setNegativeButton(getString(R.string.no)) { _, _ ->
                // do nothing
            }.create().show()
    }

    private fun navigateToLogin() {
        val intentToLogin = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intentToLogin)
    }

    private fun navigateToProfileChange() {
        startActivity(Intent(requireContext(), ProfileChangeActivity::class.java))
    }

    private fun fetchData(){
        viewModel.getCurrentUser()
    }
}