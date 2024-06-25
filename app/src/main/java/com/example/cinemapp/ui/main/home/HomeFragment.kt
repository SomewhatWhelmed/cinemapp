package com.example.cinemapp.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentHomeBinding
import com.example.cinemapp.ui.main.MainActivity
import com.example.cinemapp.ui.main.shared.MovieAdapter
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
        setupMainToolbar()
        setupAdapter()
        viewModel.setupLoading()
        viewModel.getUpcomingNextPage()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupChipGroup()
        setupOnScrollListener()

        observeFlowSafely(viewModel.state) {
            adapter.setMovies(it.movies)
            viewModel.setPagingRunning(false)
            setupLoadingVisibility(it.isLoading)
        }
    }

    private fun setupMainToolbar() {
        (activity as MainActivity).customizeTopNavigation(
            null,
            null,
            true,
            R.drawable.cinemapp_logo,
            8
        )
    }

    private fun setupLoadingVisibility(isLoading: Boolean) {
        binding.rvMovieList.isVisible = !(isLoading && viewModel.getPagesLoaded() == 1)
        binding.cpiLoading.isVisible = isLoading
    }

    private fun setupChipGroup() {
        binding.chipUpcoming.isChecked = true
        binding.cgMovieLists.setOnCheckedStateChangeListener { _, checkedId ->
            onChipChanged(checkedId[0])
        }
    }

    private fun setupAdapter() {
        binding.rvMovieList.adapter = adapter
        binding.rvMovieList.layoutManager = GridLayoutManager(context, 2)
        observeFlowSafely(adapter.onMovieCardClick) {
            onMovieCardClick(it)
        }
    }

    private fun setupOnScrollListener() {
        binding.rvMovieList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (binding.rvMovieList.isEndOfScroll()) {
                        viewModel.onScrolledToNext()
                    }
                }
            }
        )
    }

    private fun onChipChanged(checked: Int) {
        with(binding) {
            viewModel.setupLoading()
            when (checked) {
                chipUpcoming.id -> viewModel.getUpcomingNextPage()
                chipPopular.id -> viewModel.getPopularNextPage()
                chipTopRated.id -> viewModel.getTopRatedNextPage()
            }
            val smoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            smoothScroller.targetPosition = 0
            rvMovieList.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun onMovieCardClick(movie: MovieCard) {
        findNavController().safeNavigateWithArgs(
            HomeFragmentDirections.toDetailsFragment(movieId = movie.id)
        )
    }
}