package com.example.cinemapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.databinding.FragmentHomeBinding
import com.example.cinemapp.ui.main.model.MovieCard
import com.example.cinemapp.util.isEndOfScroll
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.safeNavigateWithArgs
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
        viewModel.getUpcomingNextPage()
        setupAdapter()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        observeFlowSafely(viewModel.state) {
            adapter.setMovies(it.movies)
            viewModel.togglePagingRunning()
        }
        binding.rvMovieList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (binding.rvMovieList.isEndOfScroll()) {
                        viewModel.getUpcomingNextPage()
                    }
                }
            }
        )
    }


    private fun setupAdapter() {
        binding.rvMovieList.adapter = adapter
        binding.rvMovieList.layoutManager = GridLayoutManager(context, 2)
        observeFlowSafely(adapter.onMovieCardClick) {
            onMovieCardClick(it)
        }
    }

    private fun onMovieCardClick(movie: MovieCard) {
        findNavController().safeNavigateWithArgs(
            HomeFragmentDirections.toDetailsFragment(movieId = movie.id ?: -1)
        )
    }
}