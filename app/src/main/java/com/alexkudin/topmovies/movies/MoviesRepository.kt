package com.alexkudin.topmovies.movies

import androidx.lifecycle.MutableLiveData
import com.alexkudin.topmovies.ServerApi
import com.alexkudin.topmovies.data.Movie
import com.alexkudin.topmovies.helpers.Constants
import com.alexkudin.topmovies.helpers.DateController
import com.alexkudin.topmovies.helpers.parseJsonToMovies
import com.google.gson.JsonIOException
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoviesRepository {

    private val moviesLiveData = MutableLiveData<ArrayList<Movie>?>()
    private val disposableContainer = CompositeDisposable()

    fun cancelTransactions() {
        disposableContainer.clear()
    }

    // TODO: use pagination
    fun downloadMovies(token: String): MutableLiveData<ArrayList<Movie>?> {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.MAIN_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val serverApi = retrofit.create(ServerApi::class.java)
        val resultMovies = arrayListOf<Movie>()

        disposableContainer.add(
            // download all popular movies
            serverApi.getTopRated(token = "Bearer $token")
                .toObservable()
                .flatMap { json ->
                    Observable.create<Movie> {
                        try {
                            val movies = json.parseJsonToMovies()
                            movies.forEach { movie ->
                                it.onNext(movie)
                            }
                            it.onComplete()
                        } catch (e: JsonIOException) {
                            it.onError(e)
                        }
                    }
                }
                // choose movies from 2019
                .filter {
                    DateController.getSecondaryDate(it.date ?: "") == 2019
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    resultMovies.add(it)
                }, {
                    it.printStackTrace()
                    moviesLiveData.postValue(null)
                }, {
                    moviesLiveData.postValue(resultMovies)
                })
        )

        return moviesLiveData
    }
}