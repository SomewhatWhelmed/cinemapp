package com.example.cinemapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cinemapp.databinding.FragmentSearchBinding
import com.example.cinemapp.ui.main.model.SearchCard
import com.example.cinemapp.ui.main.model.SearchType
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.safeNavigateWithArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

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
        setupAdapter()
        observeOnClickEvents()
        return binding.root
    }

    override fun onStart() {
        super.onStart()

    }

    private fun setupAdapter() {
        binding.rvCardList.adapter = adapter
        binding.rvCardList.layoutManager = GridLayoutManager(context, 2)
    }

    private fun observeOnClickEvents() {
        observeFlowSafely(adapter.onSearchCardClick) {
            onSearchCardClick(it)
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
}