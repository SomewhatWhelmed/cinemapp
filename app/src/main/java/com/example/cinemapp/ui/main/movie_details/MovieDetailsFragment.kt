package com.example.cinemapp.ui.main.movie_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.math.MathUtils
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentDetailsBinding
import com.example.cinemapp.ui.main.MainActivity
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.ui.main.model.MovieDetails
import com.example.cinemapp.util.formatRating
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.safeNavigateWithArgs
import com.example.cinemapp.util.setExpandableTextView
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel by viewModel<MovieDetailsViewModel>()
    private val castAdapter: CastAdapter by inject()
    private val mediaAdapter = MediaAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        scores = arrayOf(getString(R.string.movie_rating_none), "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        setupMainToolbar()
        setupAdapter()
        setupOnClickListeners()
        viewModel.setupLoading()
        viewModel.getMovieDetails(args.movieId)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeFlowSafely(viewModel.state) {
            it.details?.let { details ->
                setupDetails(details)
            }
            setupLoadingVisibility(it.isLoading)
            setupUserListsViews(it.isInFavorite, it.isInWatchlist, it.userRating)
            castAdapter.setCast(it.cast)
            mediaAdapter.setMedia(it.media)
        }
    }

    private fun setupMainToolbar() {
        (activity as? MainActivity)?.customizeTopNavigation(
            title = resources.getString(R.string.title_movie_details),
            navigationIconId = R.drawable.vic_arrow_back,
            isTitleCentered = false
        )
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
                phrase = getString(R.string.description_expand_more),
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
        with(binding) {
            setupButtonColor(
                cvUserRating,
                ivUserRating,
                R.color.md_theme_onPrimary,
                R.color.md_theme_surface,
                R.drawable.vic_rating,
                R.drawable.vic_rating_empty,
                userRating?.let { true } ?: false
            )
            userRating?.let {
                ivUserRating.visibility = View.INVISIBLE
                tvUserRating.text = it.toString()
                tvUserRatingLabel.text = getString(R.string.movie_rating_label_rated)
            } ?: run {
                tvUserRatingLabel.text = getString(R.string.movie_rating_label_unrated)
                ivUserRating.visibility = View.VISIBLE
                tvUserRating.text = ""
            }
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
        binding.tvFavoriteLabel.text =
            getString(if (isInFavorite) R.string.movie_favorites_remove else R.string.movie_favorites_add)
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
        binding.tvWatchlistLabel.text =
            getString(if (isInWatchlist) R.string.movie_watchlist_remove else R.string.movie_watchlist_add)
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
            ivArrowLeft.setOnClickListener {
                moveRecyclerView(-1)
            }
            ivArrowRight.setOnClickListener {
                moveRecyclerView(1)
            }
            cvFavorite.setOnClickListener {
                viewModel.setFavorite(args.movieId)
            }
            cvWatchlist.setOnClickListener {
                viewModel.setWatchlist(args.movieId)
            }
            cvUserRating.setOnClickListener {
                AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
                    .setTitle("Select rating")
                    .setPositiveButton("Confirm") { dialog, _ ->
                        viewModel.setRating(
                            args.movieId,
                            (dialog as AlertDialog).listView.checkedItemPosition
                        )
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .setSingleChoiceItems(scores, 0) { _, _ -> }.create().show()
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

    private fun setupLoadingVisibility(isLoading: Boolean) {
        with(binding) {
            clContent.isVisible = !isLoading
            cpiLoading.isVisible = isLoading
            lifecycleScope.launch {
                clListControls.isVisible =
                    viewModel.session.firstOrNull()?.let { !isLoading } ?: false
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
        private lateinit var scores: Array<String>
    }
}