package com.example.cinemapp.ui.splash

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cinemapp.BuildConfig
import com.example.cinemapp.R
import com.example.cinemapp.data.MovieRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            val repo = MovieRepository()
            val temp = repo.getUpcoming()
            Log.i("RESULTS", temp.toString())
        }
    }
}