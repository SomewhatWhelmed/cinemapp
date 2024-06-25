package com.example.cinemapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cinemapp.R
import com.example.cinemapp.databinding.ActivityMainBinding
import com.example.cinemapp.databinding.DialogAccountBinding
import com.example.cinemapp.ui.MainViewModel
import com.example.cinemapp.ui.authentication.AuthenticationActivity
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.util.finishThenStart
import com.example.cinemapp.util.loadImage
import com.example.cinemapp.util.observeFlowSafely
import kotlinx.coroutines.launch
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
        viewModel.getAccountDetail(false)
    }

    override fun onStart() {
        super.onStart()
        setupOnClickListeners()
        observeEvents()

        observeFlowSafely(viewModel.state) {
            it.accountDetails?.let { accountDetails ->
                setupAvatar(accountDetails, binding.tvInitial, binding.ivAvatar)
            }
        }

    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setupAvatar(
        accountDetails: AccountDetails?,
        tvInitial: TextView,
        ivAvatar: ImageView,
    ) {
        accountDetails?.let { details ->
            if (details.avatar.isEmpty()) {
                tvInitial.text =
                    (if (details.name.isEmpty()) details.username else details.name)
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
            ivAvatar.setColorFilter(
                ContextCompat.getColor(
                    this@MainActivity,
                    R.color.md_theme_onPrimary
                )
            )
        }
    }

    private fun setupOnClickListeners() {
        binding.rlAvatar.setOnClickListener {
            viewModel.getAccountDetail(true)
        }
    }

    private fun observeEvents() {
        observeDialogEvent()
        observeSignOutEvent()
    }

    private fun observeDialogEvent() {
        lifecycleScope.launch {
            viewModel.openAccountDialog.collect { accountDetails ->
                createDialog(accountDetails)
            }
        }
    }

    private fun observeSignOutEvent() {
        lifecycleScope.launch {
            viewModel.signOutEvent.collect {
                this@MainActivity.finishThenStart(
                    this@MainActivity,
                    AuthenticationActivity::class.java
                )
            }
        }
    }

    private fun createDialog(accountDetails: AccountDetails?) {
        val builder = AlertDialog.Builder(this, R.style.WrapContentDialog)
        val dialogBinding = DialogAccountBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root).create().show()
        with(dialogBinding) {
            accountDetails?.let {
                setupAvatar(accountDetails, tvInitial, ivAvatar)
                tvName.text = accountDetails.name
                tvUsername.text = accountDetails.username
                btnSignInOut.setOnClickListener {
                    viewModel.signOut()
                }
            } ?: run {
                rlAvatar.isVisible = false
                llNames.isVisible = false
                btnSignInOut.text = getString(R.string.sign_in)
                btnSignInOut.setOnClickListener {
                    startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
                }
            }
        }
    }

    fun isSignedIn(): Boolean = viewModel.isSignedIn()
}