package com.example.cinemapp.ui.main.settings.appearance

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentSettingsAppearanceBinding
import com.example.cinemapp.util.observeFlowSafely
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsAppearanceFragment : Fragment() {

    private val viewModel: SettingsAppearanceViewModel by viewModel()
    private var _binding: FragmentSettingsAppearanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsAppearanceBinding.inflate(inflater, container, false)
        viewModel.getCurrentTheme()
        setupOnClick()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        observeFlowSafely(viewModel.state) { state ->
            setupRadioButtons(state)
        }
    }

    private fun setupRadioButtons(themeMode: SettingsAppearanceViewModel.ThemeMode) {
        with (binding) {
            rgThemes.check(
                when (themeMode) {
                    SettingsAppearanceViewModel.ThemeMode.SYSTEM -> rbSystemDefault.id
                    SettingsAppearanceViewModel.ThemeMode.LIGHT -> rbLight.id
                    SettingsAppearanceViewModel.ThemeMode.DARK -> rbDark.id
                }
            )
        }
    }

    private fun setupOnClick() {
        with(binding) {
            rbSystemDefault.setOnClickListener {
                viewModel.setTheme(SettingsAppearanceViewModel.ThemeMode.SYSTEM)
            }
            rbLight.setOnClickListener {
                viewModel.setTheme(SettingsAppearanceViewModel.ThemeMode.LIGHT)
            }
            rbDark.setOnClickListener {
                viewModel.setTheme(SettingsAppearanceViewModel.ThemeMode.DARK)
            }
        }
    }
}