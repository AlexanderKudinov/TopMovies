package com.alexkudin.topmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alexkudin.topmovies.movies.MoviesViewModel

class ViewModelsFactory: ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) = when (modelClass) {
        MoviesViewModel::class.java -> MoviesViewModel() as T
        else -> modelClass.newInstance()
    }
}