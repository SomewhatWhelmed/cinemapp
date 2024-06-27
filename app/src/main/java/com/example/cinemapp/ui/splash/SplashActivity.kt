package com.example.cinemapp.ui.splash

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cinemapp.databinding.ActivitySplashBinding
import com.example.cinemapp.ui.authentication.AuthenticationActivity
import com.example.cinemapp.ui.main.MainActivity
import com.example.cinemapp.util.finishThenStart
import com.example.cinemapp.util.observeFlowSafely
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModel<SplashViewModel>()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.setTheme()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.handleInitialData()
    }

    override fun onStart() {
        super.onStart()
        observeGoToMainScreenEvent()
    }

    private fun observeGoToMainScreenEvent() {
        observeFlowSafely(viewModel.goToNextScreen) { validSession ->
            if (validSession) {
                finishThenStart(this@SplashActivity, MainActivity::class.java)
            } else {
                finishThenStart(this@SplashActivity, AuthenticationActivity::class.java)
            }
        }
    }
}