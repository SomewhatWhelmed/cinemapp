package com.example.cinemapp.ui.main.settings.appearance

import android.app.Activity
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentSettingsAppearanceBinding
import com.example.cinemapp.ui.main.MainActivity
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
        setupMainToolbar()
        viewModel.getCurrentTheme()
        observeCurrentThemeEvent()
        return binding.root
    }

    private fun setupMainToolbar() {
        (activity as? MainActivity)?.customizeTopNavigation(
            title = resources.getString(R.string.title_settings_appearance),
            navigationIconId = R.drawable.vic_arrow_back,
            isTitleCentered = false
        )
    }

    private fun observeCurrentThemeEvent() {
        observeFlowSafely(viewModel.currentThemeEvent) {
            setupRadioButtons(it)
            setupOnClick()
        }
    }

    private fun setupRadioButtons(themeMode: SettingsAppearanceViewModel.ThemeMode) {
        with(binding) {
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
            rgThemes.setOnCheckedChangeListener { _, checkedId ->
                viewModel.setTheme(
                    when (checkedId) {
                        rbSystemDefault.id -> SettingsAppearanceViewModel.ThemeMode.SYSTEM
                        rbLight.id -> SettingsAppearanceViewModel.ThemeMode.LIGHT
                        rbDark.id -> SettingsAppearanceViewModel.ThemeMode.DARK
                        else -> SettingsAppearanceViewModel.ThemeMode.SYSTEM
                    }
                )
            }
        }
    }
}