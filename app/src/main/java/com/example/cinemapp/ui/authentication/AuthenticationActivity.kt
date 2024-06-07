package com.example.cinemapp.ui.authentication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.cinemapp.databinding.ActivityAuthenticationBinding
import com.example.cinemapp.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthenticationActivity : AppCompatActivity() {

    private var _binding: ActivityAuthenticationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<AuthenticationViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            btnSignIn.setOnClickListener {
                viewModel.attemptSignIn(
                    etUsername.text.toString(),
                    etPassword.text.toString()
                )
            }

            tvGuest.setOnClickListener{
                finish()
                startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
            }

            tvSignUp.setOnClickListener{
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/signup")))
            }
        }

        observeSignIn()
    }

    private fun observeSignIn() {
        lifecycleScope.launch() {
            viewModel.signInAttempt.collect { success ->
                if (success) {
                    finish()
                    startActivity(Intent(this@AuthenticationActivity, MainActivity::class.java))
                } else {
                    viewModel.viewModelScope.launch(Dispatchers.Main) {
                        val toast = Toast.makeText(this@AuthenticationActivity, ERROR_MESSAGE, Toast.LENGTH_LONG)
                        toast.show()
                    }
                }
            }
        }
    }

    companion object {
        private const val ERROR_MESSAGE =
            "Invalid username and/or password: You did not provide a valid login."
    }
}