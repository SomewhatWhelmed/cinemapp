package com.example.cinemapp.ui.main.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemapp.R
import com.example.cinemapp.databinding.CardSearchBinding
import com.example.cinemapp.ui.main.model.SearchCard
import com.example.cinemapp.ui.main.model.SearchType
import com.example.cinemapp.util.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var results: List<SearchCard> = emptyList()

    private val _onSearchCardClick: MutableSharedFlow<SearchCard> = MutableSharedFlow()
    val onSearchCardClick = _onSearchCardClick.asSharedFlow()


    @SuppressLint("NotifyDataSetChanged")
    fun setResults(results: List<SearchCard>) {
        this.results = results
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = CardSearchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int {
        return results.size
    }

    inner class SearchViewHolder(private val binding: CardSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: SearchCard) {
            with(binding) {
                root.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        _onSearchCardClick.emit(result)
                    }
                }
                tvText.text = result.text
                loadImage(
                    result.image,
                    ivImage,
                    root.context,
                    when (result.type) {
                        SearchType.ACTOR -> R.drawable.ic_placeholder_person
                        SearchType.MOVIE -> R.drawable.ic_placeholder_movie
                    }
                )
            }
        }
    }
}