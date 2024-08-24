package com.example.cinemapp.ui.main.actor_details

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.CardCreditBinding
import com.example.cinemapp.ui.main.model.CastMovieCredit
import com.example.cinemapp.util.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CreditsAdapter :
    RecyclerView.Adapter<CreditsAdapter.CreditViewHolder>() {

    private var credits: List<CastMovieCredit> = emptyList()

    private val _onCardClick: MutableSharedFlow<CastMovieCredit> = MutableSharedFlow()
    val onCardClick = _onCardClick.asSharedFlow()


    @SuppressLint("NotifyDataSetChanged")
    fun setCredits(credits: List<CastMovieCredit>) {
        this.credits = credits
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        val binding = CardCreditBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        holder.bind(credits[position])
    }

    override fun getItemCount(): Int {
        return credits.size
    }

    inner class CreditViewHolder(val binding: CardCreditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieCredit: CastMovieCredit) {
            with(binding) {
                root.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        _onCardClick.emit(movieCredit)
                    }
                }
                if (movieCredit.title.isEmpty()) tvName.isVisible = false
                else tvName.text = movieCredit.title
                if (movieCredit.character.isEmpty()) tvCharacter.isVisible = false
                else tvCharacter.text = movieCredit.character
                loadImage(
                    movieCredit.posterPath,
                    ivPicture,
                    root.context,
                    R.drawable.ic_placeholder_movie
                )
            }
        }
    }
}