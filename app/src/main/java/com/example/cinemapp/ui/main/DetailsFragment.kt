package com.example.cinemapp.ui.main

import android.graphics.Color
import android.graphics.Paint
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cinemapp.BuildConfig
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentDetailsBinding
import com.example.cinemapp.util.observeFlowSafely
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModel<DetailsViewModel>()
    private val adapter = CastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        viewModel.getMovieDetails(args.movieId)
        setupAdapter()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeFlowSafely(viewModel.state) {
            it.details?.let { details ->
                setupView(details)
            }
            adapter.setCast(it.cast)
        }
    }

    private fun setupView(details: MovieDetails) {
        with(binding) {
            tbTop.setNavigationIcon(R.drawable.ic_arrow_back)
            tbTop.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            tvTitle.text = details.title
            tvGenres.text = details.genres.joinToString(", ") { genre -> genre.name }
            val runtime =
                "${(details.runtime / 60)}h ${details.runtime % 60}min"
            tvRuntime.text = runtime
            Glide.with(root.context)
                .load(details.backdropPath)
                .into(binding.ivBackdrop)

            if (details.overview.length > OVERVIEW_MAX_CHARACTERS) {
                val spannableString = makeOverview(details.overview) {
                    tvOverview.text = details.overview
                }
                tvOverview.movementMethod = LinkMovementMethod.getInstance()
                tvOverview.setText(spannableString, TextView.BufferType.SPANNABLE)
            } else tvOverview.text = details.overview
        }
    }

    private fun setupAdapter() {
        binding.rvCast.adapter = adapter
        binding.rvCast.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        observeFlowSafely(adapter.onCardClick) {

        }
    }

    private fun makeOverview(
        text: String,
        phrase: String = "More",
        phraseColor: Int = requireContext().getColor(R.color.md_theme_tertiary),
        maxChars: Int = OVERVIEW_MAX_CHARACTERS,
        onClickEvent: () -> Unit
    ): SpannableString {

        val shortText = "${text.substring(0, maxChars - 1)}... $phrase"
        val spannableString = SpannableString(shortText)

        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.color = phraseColor
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
                ds.isUnderlineText = false
            }

            override fun onClick(widget: View) {
                onClickEvent()
            }
        }
        val start = shortText.length - phrase.length
        val end = shortText.length
        spannableString.setSpan(
            clickableSpan,
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }

    companion object {
        private const val OVERVIEW_MAX_CHARACTERS = 200
    }
}