package com.example.cinemapp.ui.main.actor_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentActorDetailsBinding
import com.example.cinemapp.ui.main.MainActivity
import com.example.cinemapp.ui.main.model.CastMovieCredit
import com.example.cinemapp.ui.main.model.PersonDetails
import com.example.cinemapp.util.ageAndLifespanFormat
import com.example.cinemapp.util.loadImage
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.safeNavigateWithArgs
import com.example.cinemapp.util.setExpandableTextView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActorDetailsFragment : Fragment() {

    private val viewModel by viewModel<ActorDetailsViewModel>()
    private var _binding: FragmentActorDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ActorDetailsFragmentArgs by navArgs()
    private val creditAdapter: CreditsAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorDetailsBinding.inflate(inflater, container, false)
        setupMainToolbar()
        setupAdapter()
        viewModel.setupLoading()
        viewModel.getPersonDetails(args.personId)
        observeOnClickEvents()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observeFlowSafely(viewModel.state) {
            it.details?.let { details ->
                setupView(details)
            }
            setupLoadingVisibility(it.isLoading)
            val adapter = binding.spinnerYear.adapter as ArrayAdapter<String>
            adapter.clear()
            adapter.add(resources.getString(R.string.all_movies))
            adapter.addAll(
                it.creditYears.map { year -> year?.toString() ?: resources.getString(R.string.unannounced) }
            )
            creditAdapter.setCredits(it.credits)
        }
    }

    private fun setupMainToolbar() {
        (activity as? MainActivity)?.customizeTopNavigation(
            title = resources.getString(R.string.title_actor_details),
            navigationIconId = R.drawable.vic_arrow_back,
            isTitleCentered = false
        )
    }

    private fun setupLoadingVisibility(isLoading: Boolean) {
        binding.clContent.isVisible = !isLoading
        binding.cpiLoading.isVisible = isLoading
    }

    private fun setupView(details: PersonDetails) {
        with(binding) {
            tvName.text = details.name
            val ageDisplay = details.birthday.ageAndLifespanFormat(details.deathday)
            tvAge.text = ageDisplay
            tvGender.text = details.gender

            setExpandableTextView(
                text = details.biography,
                phrase = getString(R.string.description_expand_more),
                phraseColor = requireContext().getColor(R.color.md_theme_tertiary),
                maxChars = BIOGRAPHY_MAX_CHARACTERS,
                textView = tvBiography
            ) {
                tvBiography.text = details.biography
            }
            loadImage(
                details.profilePath,
                ivPicture,
                root.context,
                R.drawable.ic_placeholder_person
            )
        }
    }

    private fun setupAdapter() {
        with(binding) {
            val adapter = ArrayAdapter(
                root.context,
                org.koin.android.R.layout.support_simple_spinner_dropdown_item,
                listOf(resources.getString(R.string.all_movies)) +
                    viewModel.getCreditYears()
                        .map { year -> year?.toString() ?: resources.getString(R.string.unannounced) }
            )

            spinnerYear.adapter = adapter
            spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(viewModel.getCreditYears().isNotEmpty()){
                        viewModel.getCreditsFromYear(
                            personId = args.personId,
                            year = viewModel.getCreditYears()[(position - 1).coerceAtLeast(0)],
                            getAll = (position == 0)
                        )
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            rvCredits.adapter = creditAdapter
            rvCredits.layoutManager =
                GridLayoutManager(context, 3, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeOnClickEvents() {
        observeFlowSafely(creditAdapter.onCardClick) {
            onCreditClick(it)
        }
    }

    private fun onCreditClick(castMovieCredit: CastMovieCredit) {
        findNavController().safeNavigateWithArgs(
            ActorDetailsFragmentDirections.toDetailsFragment(movieId = castMovieCredit.id)
        )
    }

    companion object {
        private const val BIOGRAPHY_MAX_CHARACTERS = 200
    }
}