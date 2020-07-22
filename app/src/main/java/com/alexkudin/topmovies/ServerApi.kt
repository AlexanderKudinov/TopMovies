package com.alexkudin.topmovies

import com.google.gson.JsonObject
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface ServerApi {

    @GET("movie/popular")
    fun getTopRated(@Header("Authorization") token: String): Single<JsonObject>
}