package com.example.cinemapp.ui.main.movie_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.math.MathUtils
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentDetailsBinding
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.ui.main.model.MovieDetails
import com.example.cinemapp.util.formatRating
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.safeNavigateWithArgs
import com.example.cinemapp.util.setExpandableTextView
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel by viewModel<MovieDetailsViewModel>()
    private val castAdapter = CastAdapter()
    private val mediaAdapter = MediaAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        setupVisibility()
        viewModel.getMovieDetails(args.movieId)
        setupAdapter()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupOnClickListeners()

        observeFlowSafely(viewModel.state) {
            it.details?.let { details ->
                setupDetails(details)
                binding.clContent.visibility = View.VISIBLE
                binding.cpiLoading.visibility = View.GONE
            }

            setupUserListsViews(it.isInFavorite, it.isInWatchlist, it.userRating)
            castAdapter.setCast(it.cast)
            mediaAdapter.setMedia(it.media)
        }
    }

    private fun setupDetails(details: MovieDetails) {
        with(binding) {
            tvTitle.text = details.title
            tvGenres.text = details.genres.joinToString(", ") { genre -> genre.name }
            val runtime =
                "${(details.runtime / 60)}h ${details.runtime % 60}min"
            tvRuntime.text = runtime
            tvRating.text = formatRating(details.voteAverage)


            setExpandableTextView(
                text = details.overview,
                phraseColor = requireContext().getColor(R.color.md_theme_tertiary),
                maxChars = OVERVIEW_MAX_CHARACTERS,
                textView = tvOverview
            ) {
                tvOverview.text = details.overview
            }
        }
    }

    private fun setupUserListsViews(
        isInFavorite: Boolean,
        isInWatchlist: Boolean,
        userRating: Int?
    ) {
        setupRatingButton(userRating)
        setupFavoriteButton(isInFavorite)
        setupWatchlistButton(isInWatchlist)
    }

    private fun setupRatingButton(userRating: Int?) {
        setupButtonColor(
            binding.cvUserRating,
            binding.ivUserRating,
            R.color.md_theme_onPrimary,
            R.color.md_theme_surface,
            R.drawable.vic_rating,
            R.drawable.vic_rating_empty,
            userRating?.let { true } ?: false
        )
        userRating?.let {
            binding.ivUserRating.visibility = View.INVISIBLE
            binding.tvUserRating.text = it.toString()
            binding.tvUserRatingLabel.text = "Your rating"
        }
    }

    private fun setupFavoriteButton(isInFavorite: Boolean) {
        setupButtonColor(
            binding.cvFavorite,
            binding.ivFavorite,
            R.color.md_theme_onPrimary,
            R.color.md_theme_surface,
            R.drawable.vic_favorite,
            R.drawable.vic_favorite_empty,
            isInFavorite
        )
    }
    private fun setupWatchlistButton(isInWatchlist: Boolean) {
        setupButtonColor(
            binding.cvWatchlist,
            binding.ivWatchlist,
            R.color.md_theme_onPrimary,
            R.color.md_theme_surface,
            R.drawable.vic_watchlist,
            R.drawable.vic_watchlist_empty,
            isInWatchlist
        )
    }

    private fun setupButtonColor(
        materialCardView: MaterialCardView,
        imageView: ImageView,
        contrastColorId: Int,
        backgroundColorId: Int,
        imageFullId: Int,
        imageEmptyId: Int,
        isPressed: Boolean
    ) {
        materialCardView.setCardBackgroundColor(
            requireContext().getColor(
                if (isPressed) contrastColorId else backgroundColorId
            )
        )
        imageView.setImageResource(if (isPressed) imageFullId else imageEmptyId)
        imageView.setColorFilter(
            requireContext().getColor(
                if (isPressed) backgroundColorId else contrastColorId
            ), android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    private fun setupOnClickListeners() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
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

            observeFlowSafely(castAdapter.onCardClick) {
                onCastClick(it)
            }

            rvMedia.adapter = mediaAdapter
            rvMedia.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            PagerSnapHelper().attachToRecyclerView(rvMedia)
        }
    }

    private fun setupVisibility() {
        with(binding) {
            clContent.visibility = View.INVISIBLE
            lifecycleScope.launch {
                clListControls.visibility =
                    viewModel.session.firstOrNull()?.let { View.VISIBLE } ?: View.GONE
            }
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

    private fun onCastClick(castMember: CastMember) {
        findNavController().safeNavigateWithArgs(
            MovieDetailsFragmentDirections.toActorDetailsFragment(personId = castMember.id)
        )
    }

    companion object {
        private const val OVERVIEW_MAX_CHARACTERS = 200
        private val scores = listOf("Add rating", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    }
}