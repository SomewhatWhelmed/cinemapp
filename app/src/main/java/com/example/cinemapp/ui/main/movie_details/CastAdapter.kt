package com.example.cinemapp.ui.main.movie_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.CardPersonBinding
import com.example.cinemapp.ui.main.model.CastMember
import com.example.cinemapp.util.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CastAdapter : RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private var cast: List<CastMember> = emptyList()

    private val _onCardClick: MutableSharedFlow<CastMember> = MutableSharedFlow()
    val onCardClick = _onCardClick.asSharedFlow()


    @SuppressLint("NotifyDataSetChanged")
    fun setCast(cast: List<CastMember>) {
        this.cast = cast
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = CardPersonBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(cast[position])
        if (position == 0)
                (holder.binding.cvCardPerson.layoutParams as ViewGroup.MarginLayoutParams).marginStart += 10

    }

    override fun getItemCount(): Int {
        return cast.size
    }

    inner class CastViewHolder(val binding: CardPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(castMember: CastMember) {
            with(binding) {
                root.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        _onCardClick.emit(castMember)
                    }
                }
                tvName.text = castMember.name
                tvCharacter.text = castMember.character
                loadImage(
                    castMember.profilePath,
                    ivPicture,
                    root.context,
                    R.drawable.ic_placeholder_person
                )
            }
        }
    }
}