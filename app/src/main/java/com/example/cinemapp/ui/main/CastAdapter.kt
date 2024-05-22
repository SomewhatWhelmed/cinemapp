package com.example.cinemapp.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemapp.databinding.CardCastBinding
import com.example.cinemapp.ui.main.model.CastMember
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
        val binding = CardCastBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        holder.bind(cast[position])
    }

    override fun getItemCount(): Int {
        return cast.size
    }

    inner class CastViewHolder(private val binding: CardCastBinding) :
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
                Glide.with(binding.root.context)
                    .load(castMember.profilePath)
                    .into(binding.ivPicture)
            }
        }
    }
}