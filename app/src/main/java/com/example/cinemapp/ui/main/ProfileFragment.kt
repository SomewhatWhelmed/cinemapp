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
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            btnSignOut.visibility = View.INVISIBLE
            btnSignOut.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.userPrefs.deleteSessionId()
                    startActivity(Intent(context, AuthenticationActivity::class.java))
                }
            }
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.session.collect { sessionId ->
                    tvUsername.text = sessionId ?: "Not signed in"

                    sessionId?.let {
                        btnSignOut.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}