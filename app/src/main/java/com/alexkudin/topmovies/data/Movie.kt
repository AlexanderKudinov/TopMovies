package com.alexkudin.topmovies.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var id: Long = 0,
    var title: String? = null,
    var description: String? = null,
    var image: String? = null,
    var date: String? = null,
    var rating: Double? = null
): Parcelable