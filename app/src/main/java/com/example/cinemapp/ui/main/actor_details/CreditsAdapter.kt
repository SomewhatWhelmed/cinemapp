package com.example.cinemapp.ui.main.actor_details

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.CardPersonBinding
import com.example.cinemapp.ui.main.model.CastMovieCredit
import com.example.cinemapp.ui.main.movie_details.CastAdapter
import com.example.cinemapp.util.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CreditsAdapter(private val context: Context) :
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
        val binding = CardPersonBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        holder.bind(credits[position])
        if (position == 0) {
            val marginParams =
                holder.binding.cvCardPerson.layoutParams as ViewGroup.MarginLayoutParams
            marginParams.leftMargin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                FIRST_ITEM_MARGIN_MOD * BASE_CARD_MARGIN,
                context.resources.displayMetrics
            ).toInt()
        }
    }

    override fun getItemCount(): Int {
        return credits.size
    }

    inner class CreditViewHolder(val binding: CardPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieCredit: CastMovieCredit) {
            with(binding) {
                root.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        _onCardClick.emit(movieCredit)
                    }
                }
                tvName.text = movieCredit.title
                tvCharacter.text = movieCredit.character
                loadImage(
                    movieCredit.posterPath,
                    ivPicture,
                    root.context,
                    R.drawable.ic_placeholder_movie
                )
            }
        }
    }

    companion object {
        private const val BASE_CARD_MARGIN = 5
        private const val FIRST_ITEM_MARGIN_MOD = 2.0f
    }
}