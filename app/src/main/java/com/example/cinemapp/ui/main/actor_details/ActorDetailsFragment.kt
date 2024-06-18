package com.example.cinemapp.ui.main.actor_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentActorDetailsBinding
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
    private val creditAdapter:CreditsAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPersonDetails(args.personId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorDetailsBinding.inflate(inflater, container, false)
        binding.clContent.visibility = View.INVISIBLE
        setupAdapter()
        observeOnClickEvents()
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
            val adapter = binding.spinnerYear.adapter as ArrayAdapter<String>
            adapter.clear()
            adapter.addAll(it.creditYears.map { year -> year?.toString() ?: "Unannounced" })

            creditAdapter.setCredits(it.credits)
        }
    }

    private fun setupView(details: PersonDetails) {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            tvName.text = details.name
            val ageDisplay = details.birthday.ageAndLifespanFormat(details.deathday)
            tvAge.text = ageDisplay
            tvGender.text = details.gender

            setExpandableTextView(
                text = details.biography,
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
                viewModel.state.value.creditYears.map { year -> year?.toString() ?: "Unannounced" })

            spinnerYear.adapter = adapter
            spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.getCreditsFromYear(
                        args.personId,
                        viewModel.state.value.creditYears[position]
                    )
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            rvCredits.adapter = creditAdapter
            rvCredits.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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