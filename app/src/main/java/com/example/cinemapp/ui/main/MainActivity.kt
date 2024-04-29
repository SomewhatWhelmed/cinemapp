package com.example.cinemapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.example.cinemapp.R
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val movieRepository: MovieRepository by inject<MovieRepository>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private object NavRoutes {
        const val HOME = "home"
        const val HOME_LABEL = "Home"
        const val SEARCH = "search"
        const val SEARCH_LABEL = "Search"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNavGraph()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.niHome -> navController.navigate(NavRoutes.HOME)
                R.id.niSearch -> navController.navigate(NavRoutes.SEARCH)
            }
            true
        }
    }

    private fun createNavGraph() {
//        Doesn't work when called from onCreate
//        navController = findNavController(binding.navHostFragment.id)
        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        navController.graph = navController.createGraph(startDestination = NavRoutes.HOME) {
            fragment<HomeFragment>(NavRoutes.HOME) {
                label = NavRoutes.HOME_LABEL
            }
            fragment<SearchFragment>(NavRoutes.SEARCH) {
                label = NavRoutes.SEARCH_LABEL
            }
//            fragment<PlantDetailFragment>("${nav_routes.plant_detail}/{${nav_arguments.plant_id}}") {
//                label = resources.getString(R.string.plant_detail_title)
//                argument(nav_arguments.plant_id) {
//                    type = NavType.StringType
//                }
//            }
        }
    }
}