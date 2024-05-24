package com.example.cinemapp.ui.main

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentActorDetailsBinding
import com.example.cinemapp.ui.main.model.PersonDetails
import com.example.cinemapp.util.ageAndRangeUntil
import com.example.cinemapp.util.loadImage
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.setPattern
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class ActorDetailsFragment : Fragment() {

    private val viewModel by viewModel<ActorDetailsViewModel>()
    private var _binding: FragmentActorDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ActorDetailsFragmentArgs by navArgs()

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
        }
    }

    private fun setupView(details: PersonDetails) {
        with(binding) {
            toolbar.setNavigationIcon(R.drawable.vic_arrow_back)
            toolbar.setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            tvName.text = details.name
            val ageDisplay = details.birthday.ageAndRangeUntil(details.deathday)
            tvAge.text = ageDisplay
            tvGender.text = details.gender
            loadImage(
                details.profilePath,
                ivPicture,
                root.context,
                R.drawable.ic_placeholder_person
            )
        }
    }
}