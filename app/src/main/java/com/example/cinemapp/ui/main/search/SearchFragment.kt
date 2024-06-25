package com.example.cinemapp.ui.main.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentSearchBinding
import com.example.cinemapp.ui.main.MainActivity
import com.example.cinemapp.ui.main.model.SearchCard
import com.example.cinemapp.ui.main.model.SearchType
import com.example.cinemapp.util.isEndOfScroll
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.safeNavigateWithArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Timer
import java.util.TimerTask


class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()
    private val adapter = SearchAdapter()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupMainToolbar()
        setupAdapter()
        viewModel.setupLoading(false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupSearchBar()
        observeOnClickEvents()

        observeFlowSafely(viewModel.state) {
            adapter.setResults(it.list)
            viewModel.setPagingRunning(false)
            setupLoadingVisibility(it.isLoading)
        }

        binding.rvCardList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (binding.rvCardList.isEndOfScroll()) {
                        viewModel.getNextPage()
                    }
                }
            }
        )
    }

    private fun setupMainToolbar() {
        (activity as MainActivity).showTopNavigation(false)
    }

    private fun setupSearchBar() {
        binding.cgCategory.setOnCheckedStateChangeListener { _, checkedId ->
            onChipChanged(checkedId[0])
        }
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private var timer = Timer()
            private val DELAY: Long = 1000

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.setupLoading(true)
                    viewModel.getNextPage(query = query)
                }
                hideKeyboard(requireActivity())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.length >= 3) {
                        viewModel.setupLoading(true)
                        timer.cancel()
                        timer = Timer()
                        timer.schedule(
                            object : TimerTask() {
                                override fun run() {
                                    viewModel.getNextPage(query = it)
                                }
                            }, DELAY
                        )
                    }
                }
                return true
            }

        })
    }

    private fun setupAdapter() {
        binding.rvCardList.adapter = adapter
        binding.rvCardList.layoutManager = GridLayoutManager(context, 3)
    }

    private fun observeOnClickEvents() {
        observeFlowSafely(adapter.onSearchCardClick) {
            onSearchCardClick(it)
        }
    }

    private fun onChipChanged(checked: Int) {
        with(binding) {
            viewModel.setupLoading(true)
            when (checked) {
                chipActors.id -> viewModel.getActorsNextPage()
                chipMovies.id -> viewModel.getMoviesNextPage()
            }
            val smoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            smoothScroller.targetPosition = 0
            rvCardList.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

    private fun onSearchCardClick(searchCard: SearchCard) {
        findNavController().safeNavigateWithArgs(
            when (searchCard.type) {
                SearchType.ACTOR -> SearchFragmentDirections.toActorDetailsFragment(searchCard.id)
                SearchType.MOVIE -> SearchFragmentDirections.toDetailsFragment(searchCard.id)
            }
        )
    }

    private fun setupLoadingVisibility(isLoading: Boolean) {
        with(binding) {
            rvCardList.isInvisible = isLoading || viewModel.isListEmpty()
            cpiLoading.isVisible = isLoading
            tvNoResults.isVisible = !isLoading && viewModel.isListEmpty()
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}