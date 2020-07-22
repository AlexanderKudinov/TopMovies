package com.alexkudin.topmovies.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexkudin.topmovies.data.Movie

class MoviesViewModel: ViewModel() {

    private var moviesLiveData: MutableLiveData<ArrayList<Movie>?>? = null
    private val repository = MoviesRepository()

    fun getMovies(): LiveData<ArrayList<Movie>?>? = moviesLiveData

    fun clearViewModel() {
        repository.cancelTransactions()
    }

    fun downloadMovies(token: String) {
        moviesLiveData = repository.downloadMovies(token = token)
    }
}