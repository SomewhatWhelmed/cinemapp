package com.example.cinemapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cinemapp.R
import com.example.cinemapp.databinding.ActivityMainBinding
import com.example.cinemapp.ui.MainViewModel
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.util.loadImage
import com.example.cinemapp.util.observeFlowSafely
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        viewModel.getAccountDetail()
    }

    override fun onStart() {
        super.onStart()

        observeFlowSafely(viewModel.state) {
            it.accountDetails?.let { accountDetails ->
                setupAvatar(accountDetails)
            }
        }

    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setupAvatar(accountDetails: AccountDetails?) {
        with(binding) {
            accountDetails?.let { details ->
                if (details.avatar.isEmpty()) {
                    tvInitial.text = (if (details.name.isEmpty()) details.username else details.name)
                        .substring(0, 1)
                    ivAvatar.setBackgroundColor(
                        ContextCompat.getColor(this@MainActivity, R.color.md_theme_onPrimary)
                    )
                    ivAvatar.setImageDrawable(null)

                } else {
                    tvInitial.text = ""
                    ivAvatar.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.md_theme_primary
                        )
                    )
                    loadImage(
                        accountDetails.avatar,
                        ivAvatar,
                        this@MainActivity,
                        R.drawable.vic_profile
                    )
                    ivAvatar.scaleX = 1f
                    ivAvatar.scaleY = 1f
                    ivAvatar.imageTintList = null
                }
            } ?: run {
                tvInitial.text = ""
                ivAvatar.setBackgroundColor(
                    ContextCompat.getColor(this@MainActivity, R.color.md_theme_primary)
                )
                ivAvatar.setImageResource(R.drawable.vic_profile)
                ivAvatar.scaleX = 1.2f
                ivAvatar.scaleY = 1.2f
                ivAvatar.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.md_theme_onPrimary))
            }

        }
    }
}