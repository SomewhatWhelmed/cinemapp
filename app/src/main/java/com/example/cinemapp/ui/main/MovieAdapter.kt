package com.example.cinemapp.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cinemapp.databinding.CardMovieBinding
import com.example.cinemapp.ui.main.model.MovieCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies: List<MovieCard> = emptyList()

    private val _onMovieCardClick: MutableSharedFlow<MovieCard> = MutableSharedFlow()
    val onMovieCardClick = _onMovieCardClick.asSharedFlow()


    @SuppressLint("NotifyDataSetChanged")
    fun setMovies(movies: List<MovieCard>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = CardMovieBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(private val binding: CardMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieCard) {
            with(binding) {
                root.setOnClickListener {
                    CoroutineScope(Dispatchers.Main).launch {
                        _onMovieCardClick.emit(movie)
                    }
                }
                tvTitle.text = movie.title
                tvCardRating.text = "%.1f".format(movie.voteAverage)
                Glide.with(binding.root.context)
                    .load(movie.posterPath)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.ivPoster)
            }
        }
    }
}