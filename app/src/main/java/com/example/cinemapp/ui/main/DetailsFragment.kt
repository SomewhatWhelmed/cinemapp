package com.example.cinemapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentDetailsBinding
import com.example.cinemapp.databinding.FragmentHomeBinding
import com.example.cinemapp.util.observeFlowSafely
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsFragment() : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModel<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        viewModel.getMovieDetails(args.movieId)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeFlowSafely(viewModel.state) {
            setupView(it.details)
        }
    }

    private fun setupView(details: MovieDetails) {
        with(binding) {
            tvTitle.text = details.title
        }
    }
}