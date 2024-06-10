package com.example.cinemapp.ui.main

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.cinemapp.R
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.databinding.FragmentProfileBinding
import com.example.cinemapp.databinding.FragmentSearchBinding
import com.example.cinemapp.ui.authentication.AuthenticationActivity
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.util.loadImage
import com.example.cinemapp.util.observeFlowSafely
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val viewModel by viewModel<ProfileViewModel>()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAccountDetails()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupVisibility()
        setupOnClick()
        observeSignOutEvent()

        observeFlowSafely(viewModel.state) {
            it.accountDetails?.let { accountDetails ->
                setupViews(accountDetails)
            }
        }
    }

    private fun setupViews(accountDetails: AccountDetails) {
        with(binding) {
            tvName.text = accountDetails.name
            tvUsername.text = accountDetails.username
            if (accountDetails.avatar.isEmpty()) {
                tvInitial.text = accountDetails.name.substring(0, 1)
            } else loadImage(
                accountDetails.avatar,
                ivProfilePicture,
                root.context,
                R.drawable.ic_placeholder_person
            )
        }
    }

    private fun setupOnClick() {
//        binding.btnSignOut.visibility = View.INVISIBLE
        binding.btnSignOut.setOnClickListener {
            lifecycleScope.launch {
                viewModel.signOut()
            }
        }
    }

    private fun setupVisibility() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.session.collect { sessionId ->
                sessionId?.let {
                    binding.btnSignOut.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun observeSignOutEvent() {
        lifecycleScope.launch {
            viewModel.signOut.collect {
                startActivity(Intent(context, AuthenticationActivity::class.java))
            }
        }
    }
}