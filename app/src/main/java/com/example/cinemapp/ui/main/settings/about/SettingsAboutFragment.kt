package com.example.cinemapp.ui.main.settings.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentSettingsAboutBinding
import com.example.cinemapp.ui.main.MainActivity

class SettingsAboutFragment : Fragment() {

    private var _binding: FragmentSettingsAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsAboutBinding.inflate(inflater, container, false)
        setupMainToolbar()
        setupVersionNumber()
        return binding.root
    }

    private fun setupMainToolbar() {
        (activity as? MainActivity)?.customizeTopNavigation(
            title = resources.getString(R.string.title_settings_about),
            navigationIconId = R.drawable.vic_arrow_back,
            isTitleCentered = false
        )
    }

    private fun setupVersionNumber() {
        activity?.let {
            binding.siVersion.setValue(it.packageManager.getPackageInfo(it.packageName, 0).versionName)
        }
    }
}