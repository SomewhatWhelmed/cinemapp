package com.example.cinemapp.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.FragmentProfileBinding
import com.example.cinemapp.ui.authentication.AuthenticationActivity
import com.example.cinemapp.ui.main.shared.MovieAdapter
import com.example.cinemapp.ui.main.model.AccountDetails
import com.example.cinemapp.ui.main.model.MovieCard
import com.example.cinemapp.util.finishThenStart
import com.example.cinemapp.util.isEndOfScroll
import com.example.cinemapp.util.loadImage
import com.example.cinemapp.util.observeFlowSafely
import com.example.cinemapp.util.safeNavigateWithArgs
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val viewModel by viewModel<ProfileViewModel>()
    private val adapter = MovieAdapter()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupVisibility()
        setupAdapter()
        viewModel.getInitialData()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupOnClick()
        observeSignedInEvent()
        observeSignOutEvent()
        setupTabs()

        observeFlowSafely(viewModel.state) {
            it.accountDetails?.let { accountDetails ->
                setupViews(accountDetails)
            }
            adapter.setMovies(it.movies)
            viewModel.setPagingRunning(false)
        }

        binding.rvMovieList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (binding.rvMovieList.isEndOfScroll()) {
                        viewModel.getNextPage()
                    }
                }
            }
        )
    }

    private fun setupViews(accountDetails: AccountDetails) {
        with(binding) {
            tvName.text = accountDetails.name
            tvUsername.text = accountDetails.username
            if (accountDetails.avatar.isEmpty()) {
                tvInitial.text = accountDetails.name.substring(0, 1)
            } else {
                loadImage(
                    accountDetails.avatar,
                    ivAvatar,
                    root.context,
                    R.drawable.ic_placeholder_person
                )
                ivAvatar.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.md_theme_surface
                    )
                )
            }
            cpiLoading.visibility = View.INVISIBLE
            clContent.visibility = View.VISIBLE
        }
    }

    private fun setupOnClick() {
        binding.btnSignOut.setOnClickListener {
            lifecycleScope.launch {
                viewModel.signOut()
            }
        }
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(context, AuthenticationActivity::class.java))
        }
    }

    private fun setupVisibility() {
        lifecycleScope.launch(Dispatchers.Main) {
            with(binding) {
                btnSignIn.visibility = View.INVISIBLE
                clContent.visibility = View.INVISIBLE
                cpiLoading.visibility = View.VISIBLE
            }
        }

    }


    private fun setupTabs() {
        with(binding) {
            tlMovieLists.selectTab(tlMovieLists.getTabAt(0))
            tlMovieLists.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let { onTabChanged(it) }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun onTabChanged(tabPosition: Int) {
        viewModel.loadPage(tabPosition)
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        smoothScroller.targetPosition = 0
        binding.rvMovieList.layoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun observeSignOutEvent() {
        lifecycleScope.launch {
            viewModel.signOut.collect {
                activity?.finishThenStart(context, AuthenticationActivity::class.java)
            }
        }
    }

    private fun observeSignedInEvent() {
        lifecycleScope.launch {
            viewModel.notSignedIn.collect {
                binding.clContent.visibility = View.INVISIBLE
                binding.cpiLoading.visibility = View.INVISIBLE
                binding.btnSignIn.visibility = View.VISIBLE
            }
        }
    }

    private fun setupAdapter() {
        binding.rvMovieList.adapter = adapter
        binding.rvMovieList.layoutManager = GridLayoutManager(context, 2)
        observeFlowSafely(adapter.onMovieCardClick) {
            onMovieCardClick(it)
        }
    }

    private fun onMovieCardClick(movie: MovieCard) {
        findNavController().safeNavigateWithArgs(
            ProfileFragmentDirections.toDetailsFragment(movieId = movie.id)
        )
    }

}

