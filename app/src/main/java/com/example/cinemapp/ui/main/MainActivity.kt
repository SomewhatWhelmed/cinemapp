package com.example.cinemapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.cinemapp.R
import com.example.cinemapp.databinding.ActivityMainBinding
import com.example.cinemapp.databinding.DialogAccountBinding
import com.example.cinemapp.ui.MainViewModel
import com.example.cinemapp.ui.authentication.AuthenticationActivity
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.util.finishThenStart
import com.example.cinemapp.util.loadImage
import com.example.cinemapp.util.mapDpToPixel
import com.example.cinemapp.util.observeFlowSafely
import com.google.android.material.navigation.NavigationBarView
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
    }

    override fun onStart() {
        super.onStart()
        viewModel.initialSetup()
        setupOnClickListeners()
        observeEvents()

        observeFlowSafely(viewModel.state) {
            setupAvatar(it.accountDetails, binding.tvInitial, binding.ivAvatar)
            if (it.isDialogOpened) {
                createDialog(it.accountDetails)
            }
        }

    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.apply {
            NavigationUI.setupWithNavController(this, navController)
            setOnItemSelectedListener { item ->
                NavigationUI.onNavDestinationSelected(item, navController)
                return@setOnItemSelectedListener true
            }
            setOnItemReselectedListener {
                navController.popBackStack(destinationId = it.itemId, inclusive = false)
            }
        }
    }

    private fun setupAvatar(
        accountDetails: AccountDetails?,
        tvInitial: TextView,
        ivAvatar: ImageView,
    ) {
        accountDetails?.let { details ->
            if (details.avatar.isEmpty()) {
                tvInitial.text =
                    (details.name.ifEmpty { details.username })
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
                ivAvatar.colorFilter = null
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
            viewModel.openAccountDialog()
        }
    }

    private fun observeEvents() {
        observeDialogEvent()
        observeSignOutEvent()
    }

    private fun observeDialogEvent() {
        observeFlowSafely(viewModel.openAccountDialog) { accountDetails ->
            createDialog(accountDetails)
        }
    }

    private fun observeSignOutEvent() {
        observeFlowSafely(viewModel.signOutEvent) {
            finishThenStart(
                this@MainActivity,
                AuthenticationActivity::class.java
            )
        }
    }

    private fun createDialog(accountDetails: AccountDetails?) {
        val builder = AlertDialog.Builder(this, R.style.WrapContentDialog)
        val dialogBinding = DialogAccountBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
            .setOnDismissListener { viewModel.closeAccountDialog() }
            .create().show()
        with(dialogBinding) {
            accountDetails?.let {
                setupAvatar(accountDetails, tvInitial, ivAvatar)
                tvName.text = accountDetails.name
                tvUsername.text = accountDetails.username
                btnSignInOut.text = getString(R.string.sign_out)
                btnSignInOut.setOnClickListener {
                    viewModel.signOut()
                }
            } ?: run {
                rlAvatar.isVisible = false
                llNames.isVisible = false
                btnSignInOut.text = getString(R.string.sign_in)
                btnSignInOut.setOnClickListener {
                    finishThenStart(this@MainActivity, AuthenticationActivity::class.java)
                }
            }
        }
    }

    fun isSignedIn(): Boolean = viewModel.state.value.isSignedIn

    fun customizeTopNavigation(
        title: String? = null,
        navigationIconId: Int? = null,
        isTitleCentered: Boolean = false,
        logoId: Int? = null,
        padding: Int? = null,
        ) {
        with(binding) {
            title?.let {
                toolbar.title = it
                toolbar.isTitleCentered = isTitleCentered
            } ?: run { toolbar.title = null }
            navigationIconId?.let {
                toolbar.navigationIcon = AppCompatResources.getDrawable(this@MainActivity, it)
                toolbar.setNavigationOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
            } ?: run { toolbar.navigationIcon = null }
            logoId?.let { toolbar.logo = AppCompatResources.getDrawable(this@MainActivity, it)}
                ?: run { toolbar.logo = null }
            padding?.let { toolbar.setPadding(mapDpToPixel(it.toFloat(), this@MainActivity)) }
                ?: run { toolbar.setPadding(0) }
        }
        showTopNavigation(true)
    }
    fun showTopNavigation(isVisible: Boolean) {
        binding.clToolbar.isVisible = isVisible
    }
}