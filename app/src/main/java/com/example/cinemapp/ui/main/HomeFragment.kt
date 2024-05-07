package com.example.cinemapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cinemapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel by viewModel<HomeViewModel>()
    private val adapter = MovieAdapter()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.getUpcoming()
        setupAdapter()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            viewModel.state.collect {
                adapter.setMovies(it.movies)
            }
        }
    }

    private fun setupAdapter() {
        binding.rvMovieList.adapter = adapter
        binding.rvMovieList.layoutManager = GridLayoutManager(context, 2)
    }
}