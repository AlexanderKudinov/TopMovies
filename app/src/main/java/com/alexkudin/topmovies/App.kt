package com.alexkudin.topmovies

import android.app.Application

class App: Application() {

    val viewModelsFactory by lazy { ViewModelsFactory() }
}