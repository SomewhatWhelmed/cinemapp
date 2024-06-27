package com.example.cinemapp.ui.main.collections

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.databinding.FragmentCollectionsBinding
import com.example.cinemapp.ui.authentication.AuthenticationActivity
import com.example.cinemapp.ui.main.MainActivity
import com.example.cinemapp.ui.main.shared.MovieAdapter
import com.example.cinemapp.ui.main.model.MovieCard
import com.example.cinemapp.util.isEndOfScroll
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.safeNavigateWithArgs
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionsFragment : Fragment() {

    private val viewModel by viewModel<CollectionsViewModel>()
    private val adapter = MovieAdapter()
    private var _binding: FragmentCollectionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsBinding.inflate(inflater, container, false)
        setupMainToolbar()
        setupAdapter()
        viewModel.setupLoading()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.getInitialData()
        setupOnClick()
        setupTabs()

        observeFlowSafely(viewModel.state) {
            adapter.setMovies(it.movies)
            setupLoadingVisibility(it.isLoading, (activity as? MainActivity)?.isSignedIn() ?: false)
            viewModel.setPagingRunning(false)
        }

        binding.rvMovieList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (binding.rvMovieList.isEndOfScroll() && !viewModel.isLastPage()) {
                        viewModel.setupLoading()
                        viewModel.getNextPage()
                    }
                }
            }
        )
    }

    private fun setupMainToolbar() {
        (activity as? MainActivity)?.customizeTopNavigation(
            resources.getString(R.string.title_collections),
            isTitleCentered = true
        )
    }

    private fun setupLoadingVisibility(isLoading: Boolean, signedIn: Boolean) {
        with(binding) {
            clContent.isVisible = signedIn
            llNotSignedIn.isVisible = !isLoading && !signedIn
            rvMovieList.isInvisible = isLoading
            cpiLoading.isVisible = isLoading
        }
    }

    private fun setupOnClick() {
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(context, AuthenticationActivity::class.java))
        }
    }


    private fun setupTabs() {
        with(binding) {
            tlMovieLists.selectTab(
                tlMovieLists.getTabAt(
                    when (viewModel.getCurrentListType()) {
                        MovieRepository.MovieListType.FAVORITE -> 0
                        MovieRepository.MovieListType.WATCHLIST -> 1
                        MovieRepository.MovieListType.RATED -> 2
                        else -> 0
                    }
                )
            )
            tlMovieLists.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let { onTabChanged(it) }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun onTabChanged(tabPosition: Int) {
        viewModel.setupLoading()
        viewModel.loadPage(tabPosition)
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = 0
        binding.rvMovieList.layoutManager?.startSmoothScroll(smoothScroller)
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
            CollectionsFragmentDirections.toDetailsFragment(movieId = movie.id)
        )
    }

}

