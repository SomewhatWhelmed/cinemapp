package com.example.cinemapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.cinemapp.R
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.databinding.ActivityMainBinding
import com.example.cinemapp.databinding.ActivitySplashBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val movieRepository: MovieRepository by inject<MovieRepository>()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            Log.i("RESULTS", movieRepository.getUpcoming().toString())
        }
    }
}