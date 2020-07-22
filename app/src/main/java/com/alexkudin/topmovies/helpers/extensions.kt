package com.alexkudin.topmovies.helpers

import android.util.Log
import com.alexkudin.topmovies.data.Movie
import com.alexkudin.topmovies.helpers.Constants.IMAGE_URL
import com.google.gson.JsonIOException
import com.google.gson.JsonObject

fun JsonObject.parseJsonToMovies(): ArrayList<Movie> {
    val movies = arrayListOf<Movie>()
    if (!has("results"))
        throw JsonIOException(this::class.java.toString() + " has no member \"results\"")
    val jsonMovies = getAsJsonArray("results")

    jsonMovies.forEach {
        try {
            val json = it.asJsonObject
            Log.d("tttttttttt", it.toString())
            movies.add(
                Movie(
                    id = json.get("id").asLong,
                    title = json.get("title").asString,
                    description = json.get("overview").asString,
                    image = IMAGE_URL + json.get("poster_path").asString,
                    date = DateController.parseSecondaryDateFromPrimary(json.get("release_date").asString),
                    rating = json.get("vote_average").asDouble
                )
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    if (movies.isEmpty())
        throw JsonIOException(this::class.java.toString() + " has no movies")
    return movies
}