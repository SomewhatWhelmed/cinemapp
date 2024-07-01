package com.example.cinemapp.ui.main.settings.language

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentSettingsLanguageBinding
import com.example.cinemapp.ui.main.MainActivity
import com.example.cinemapp.util.ENGLISH
import com.example.cinemapp.util.SERBIAN
import com.example.cinemapp.util.getDefaultLanguage
import com.example.cinemapp.util.observeFlowSafely
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsLanguageFragment : Fragment() {

    private val viewModel: SettingsLanguageViewModel by viewModel()
    private var _binding: FragmentSettingsLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsLanguageBinding.inflate(inflater, container, false)
        setupMainToolbar()
        viewModel.getCurrentLanguage()
        observeCurrentLanguage()
        return binding.root
    }

    private fun setupMainToolbar() {
        (activity as? MainActivity)?.customizeTopNavigation(
            title = resources.getString(R.string.title_settings_language),
            navigationIconId = R.drawable.vic_arrow_back,
            isTitleCentered = false
        )
    }

    private fun observeCurrentLanguage() {
        observeFlowSafely(viewModel.currentLanguageEvent) {
            setupRadioButtons(it)
            setupOnClick()
        }
    }

    private fun setupRadioButtons(language: String) {
        binding.apply {
            rgLanguages.check(
                when (language) {
                    ENGLISH -> rbEnglish.id
                    SERBIAN -> rbSerbian.id
                    else -> rbEnglish.id
                }
            )
        }
    }

    private fun setupOnClick() {
        binding.apply {
            rgLanguages.setOnCheckedChangeListener { _, checkedId ->
                viewModel.setLanguage(
                    when (checkedId) {
                        rbEnglish.id -> ENGLISH
                        rbSerbian.id -> SERBIAN
                        else -> getDefaultLanguage()
                    }
                )
            }
        }
    }
}