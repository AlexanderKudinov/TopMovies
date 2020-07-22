package com.alexkudin.topmovies.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alexkudin.topmovies.R
import com.alexkudin.topmovies.data.Movie
import com.squareup.picasso.Picasso
import kotlin.math.truncate

class MoviesRvAdapter: RecyclerView.Adapter<MoviesRvAdapter.MovieViewHolder>() {

    private var movieListener: MovieClickListener? = null
    private val movies = arrayListOf<Movie>()

    // can be realized by Diff Utils
    fun updateMovies(newMovies: ArrayList<Movie>?) {
        movies.clear()
        if (newMovies != null)
            movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    fun setListener(listener: MovieClickListener) {
        movieListener = listener
    }

    fun clearListener() {
        movieListener = null
    }


    override fun getItemCount() = movies.size

    override fun getItemId(position: Int) = movies[position].id

    override fun getItemViewType(position: Int) = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_card_movie,
            parent,
            false
        )
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind()
    }

    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val textTitle: TextView = itemView.findViewById(R.id.textMovieTitle_movieCard)
        private val textDescription: TextView = itemView.findViewById(R.id.textMovieDescription_movieCard)
        private val textDate: TextView = itemView.findViewById(R.id.textMovieDate_movieCard)
        private val image: ImageView = itemView.findViewById(R.id.imageMovie_movieCard)
        private val btn: LinearLayout = itemView.findViewById(R.id.layoutBtn_scheduleViewing)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressRating_movieCard)
        private val textRating: TextView = itemView.findViewById(R.id.textRating_movieCard)

        fun bind() {
            textTitle.text = movies[adapterPosition].title
            textDescription.text = movies[adapterPosition].description
            textDate.text = movies[adapterPosition].date
            textRating.text = movies[adapterPosition].rating.toString()
            progressBar.progress = truncate(movies[adapterPosition].rating ?: 0.0).toInt()

            Picasso.Builder(textTitle.context)
                .build()
                .load(movies[adapterPosition].image.toString())
                .fit()
                .centerCrop()
                .into(image)

            btn.setOnClickListener {
                movieListener?.onMovieClicked(movies[adapterPosition])
            }
        }
    }

    interface MovieClickListener {
        fun onMovieClicked(movie: Movie)
    }
}