package com.example.cinemapp.ui.main.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentSettingsMainBinding
import com.example.cinemapp.ui.main.MainActivity
import com.example.cinemapp.util.safeNavigateWithArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsMainFragment : Fragment() {

    private var _binding: FragmentSettingsMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsMainBinding.inflate(inflater, container, false)
        setupMainToolbar()
        setupOnClick()
        return binding.root
    }


    private fun setupMainToolbar() {
        (activity as? MainActivity)?.customizeTopNavigation(
            title = resources.getString(R.string.title_settings),
            isTitleCentered = true
        )
    }

    private fun setupOnClick() {
        with(binding) {
            siAppearance.setOnClickListener {
                findNavController().safeNavigateWithArgs(
                    SettingsMainFragmentDirections.toSettingsAppearanceFragment()
                )
            }
            siLanguage.setOnClickListener {
                findNavController().safeNavigateWithArgs(
                    SettingsMainFragmentDirections.toSettingsLanguageFragment()
                )
            }
            siAbout.setOnClickListener {
                findNavController().safeNavigateWithArgs(
                    SettingsMainFragmentDirections.toSettingsAboutFragment()
                )
            }
        }
    }
}