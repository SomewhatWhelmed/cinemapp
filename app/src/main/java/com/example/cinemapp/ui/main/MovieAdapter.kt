package com.example.cinemapp.ui.main

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemapp.BuildConfig
import com.example.cinemapp.data.Movie
import com.example.cinemapp.data.MovieCard
import com.example.cinemapp.databinding.CardMovieBinding
import java.util.Calendar

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var movies: List<MovieCard> = emptyList()


    @SuppressLint("NotifyDataSetChanged")
    fun setMovies(movies: List<MovieCard>){
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
                tvTitle.text = movie.title
                tvTitle.text = Html.fromHtml("<b>${movie.title}</b> (${movie.releaseDate?.let {
                    val cal = Calendar.getInstance()
                    cal.setTime(it)
                    cal.get(Calendar.YEAR)
                }})", 0)
                tvCardRating.text = "%.1f".format(movie.voteAverage)
                Glide.with(binding.root.context)
                    .load(BuildConfig.URL_BASE_IMAGE + "w500/" + movie.posterPath)
                    .into(binding.ivPoster)
            }
        }
    }
}