package com.example.cinemapp.ui.main

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.math.MathUtils
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentDetailsBinding
import com.example.cinemapp.ui.main.model.MovieDetails
import com.example.cinemapp.util.makeExpandableText
import com.example.cinemapp.util.observeFlowSafely
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModel<DetailsViewModel>()
    private val castAdapter = CastAdapter()
    private val mediaAdapter = MediaAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.clContent.visibility = View.INVISIBLE
        viewModel.getMovieDetails(args.movieId)
        setupAdapter()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeFlowSafely(viewModel.state) {
            it.details?.let { details ->
                setupView(details)
                binding.clContent.visibility = View.VISIBLE
                binding.cpiLoading.visibility = View.GONE
            }
            castAdapter.setCast(it.cast)
            mediaAdapter.setMedia(it.media)
        }
    }

    private fun setupView(details: MovieDetails) {
        with(binding) {
            toolbar.setNavigationIcon(R.drawable.vic_arrow_back)
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            tvTitle.text = details.title
            tvGenres.text = details.genres.joinToString(", ") { genre -> genre.name }
            val runtime =
                "${(details.runtime / 60)}h ${details.runtime % 60}min"
            tvRuntime.text = runtime

            if (details.overview.length > OVERVIEW_MAX_CHARACTERS) {
                val spannableString = makeExpandableText(
                    text = details.overview,
                    phraseColor = requireContext().getColor(R.color.md_theme_tertiary),
                    maxChars = OVERVIEW_MAX_CHARACTERS
                ) {
                    tvOverview.text = details.overview
                }
                tvOverview.movementMethod = LinkMovementMethod.getInstance()
                tvOverview.setText(spannableString, TextView.BufferType.SPANNABLE)
            } else tvOverview.text = details.overview

            ivArrowLeft.setOnClickListener {
                moveRecyclerView(-1)
            }
            ivArrowRight.setOnClickListener {
                moveRecyclerView(1)
            }
        }
    }

    private fun setupAdapter() {
        with(binding) {
            rvCast.adapter = castAdapter
            rvCast.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            rvMedia.adapter = mediaAdapter
            rvMedia.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            PagerSnapHelper().attachToRecyclerView(rvMedia)
        }
    }

    private fun moveRecyclerView(delta: Int) {
        with(binding) {
            val lm = rvMedia.layoutManager as LinearLayoutManager
            val smoothScroller = object : LinearSmoothScroller(context) {
                override fun getHorizontalSnapPreference(): Int {
                    return SNAP_TO_START
                }
            }
            smoothScroller.targetPosition = MathUtils.clamp(
                lm.findFirstCompletelyVisibleItemPosition() + delta,
                0,
                lm.itemCount - 1
            )

            lm.startSmoothScroll(smoothScroller)
        }
    }

    companion object {
        private const val OVERVIEW_MAX_CHARACTERS = 200
    }
}