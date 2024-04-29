package com.example.cinemapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
        setupAdapter()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch {
            viewModel.movieList.collect {
                adapter.setMovies(it)
            }
        }
    }

    private fun setupAdapter() {
        _binding?.rvMovieList?.adapter = adapter
        _binding?.rvMovieList?.layoutManager = GridLayoutManager(context, 2)
    }
}