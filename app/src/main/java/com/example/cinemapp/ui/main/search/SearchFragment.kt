package com.example.cinemapp.ui.main.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.databinding.FragmentSearchBinding
import com.example.cinemapp.ui.main.model.SearchCard
import com.example.cinemapp.ui.main.model.SearchType
import com.example.cinemapp.util.isEndOfScroll
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.safeNavigateWithArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        setVisibilityLoadingEnd()
        setupAdapter()
        observeOnClickEvents()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupViews()

        observeFlowSafely(viewModel.state) {
            adapter.setResults(it.list)
            viewModel.setPagingRunning(false)
            setVisibilityLoadingEnd()
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

    private fun setupViews() {
        binding.cgCategory.setOnCheckedStateChangeListener {_, checkedId ->
            onChipChanged(checkedId[0])
        }
        binding.svSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            private var timer = Timer()
            private val DELAY: Long = 1000

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if(it.length >= 3) {
                        setVisibilityLoadingStart()
                        viewModel.getNextPage(query = query)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let{
                    if(it.length >= 3){
                        timer.cancel()
                        timer = Timer()
                        timer.schedule(
                            object: TimerTask() {
                                override fun run() {
                                    viewModel.viewModelScope.launch(Dispatchers.Main) {
                                        setVisibilityLoadingStart()
                                    }
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
        with(binding){
            when(checked) {
                chipActors.id -> viewModel.getActorsNextPage()
                chipMovies.id -> viewModel.getMoviesNextPage()
            }
            setVisibilityLoadingStart()
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
            when(searchCard.type) {
                SearchType.ACTOR -> SearchFragmentDirections.toActorDetailsFragment(searchCard.id)
                SearchType.MOVIE -> SearchFragmentDirections.toDetailsFragment(searchCard.id)
            }
        )
    }

    private fun setVisibilityLoadingStart() {
        binding.rvCardList.visibility = View.INVISIBLE
        binding.tvNoResults.visibility = View.INVISIBLE
        binding.cpiLoading.visibility = View.VISIBLE
    }

    private fun setVisibilityLoadingEnd() {
        if(viewModel.state.value.list.isEmpty()) binding.tvNoResults.visibility = View.VISIBLE
        else binding.rvCardList.visibility = View.VISIBLE
        binding.cpiLoading.visibility = View.INVISIBLE
    }
}