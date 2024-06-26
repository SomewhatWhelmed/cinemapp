package com.example.cinemapp.ui.main.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentSettingsMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsMainFragment : Fragment() {

    private val viewModel: SettingsMainViewModel by viewModel()
    private var _binding: FragmentSettingsMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsMainBinding.inflate(inflater, container, false)

        return binding.root
    }

}