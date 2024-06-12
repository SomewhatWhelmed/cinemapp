package com.example.cinemapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.databinding.ActivitySplashBinding
import com.example.cinemapp.ui.authentication.AuthenticationActivity
import com.example.cinemapp.ui.main.MainActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.handleInitialData()
    }

    override fun onStart() {
        super.onStart()
        observeGoToMainScreenEvent()
    }

    private fun observeGoToMainScreenEvent() {
        lifecycleScope.launch {
            viewModel.gotoMainScreen.collect {
                finish()
                viewModel.session.collect { sessionId ->
                    sessionId?.let {
                        startActivity(
                            Intent(
                                this@SplashActivity,
                                MainActivity::class.java
                            )
                        )
                    } ?: startActivity(
                        Intent(
                            this@SplashActivity,
                            AuthenticationActivity::class.java
                        )
                    )
                }
            }
        }
    }
}