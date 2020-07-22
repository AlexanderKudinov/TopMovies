package com.alexkudin.topmovies.movies

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alexkudin.topmovies.AlarmManagerController
import com.alexkudin.topmovies.MainActivity
import com.alexkudin.topmovies.R
import com.alexkudin.topmovies.data.Movie
import com.alexkudin.topmovies.helpers.DateController.datePassed
import com.alexkudin.topmovies.helpers.DateController.differenceBetweenDates
import com.alexkudin.topmovies.helpers.DateController.getCurrentDate
import com.alexkudin.topmovies.helpers.DateController.timePassed
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movies.*
import java.lang.ref.WeakReference
import java.util.*

class MoviesFragment: Fragment(R.layout.fragment_movies),
    MoviesRvAdapter.MovieClickListener {

    private val viewModel by lazy {
        ViewModelProvider(this, (activity as MainActivity).app.viewModelsFactory)
            .get(MoviesViewModel::class.java)
    }
    private var adapter = MoviesRvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!adapter.hasObservers())
            adapter.setHasStableIds(true)
        adapter.setListener(this)

        viewModel.downloadMovies(resources.getString(R.string.token))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerMovies.adapter = adapter
        setObservers()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // if movies haven't been downloaded yet
        if (viewModel.getMovies()?.value == null)
            showProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearViewModel()
        adapter.clearListener()
    }



    private fun setObservers() {
        viewModel.getMovies()?.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                try {
                    Snackbar.make(
                        requireView(),
                        resources.getString(R.string.downloadingError),
                        Snackbar.LENGTH_LONG
                    ).show()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
            }

            adapter.updateMovies(newMovies = it)
            hideProgressBar()
        })
    }

    // before downloading movies
    private fun showProgressBar() {
        progressBarMovies.visibility = VISIBLE
        recyclerMovies.visibility = GONE
    }

    // after downloading movies
    private fun hideProgressBar() {
        progressBarMovies.visibility = GONE
        recyclerMovies.visibility = VISIBLE
    }

    private fun createDateDialog(movie: Movie) {
        DatePickerDialog(
            requireContext(),
            // after accepting certain date
            { view: DatePicker?, year: Int, month: Int, day: Int ->
                val calendar = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                if (datePassed(calendar = calendar)) {
                    Toast.makeText(requireContext(), resources.getString(R.string.datePassed), Toast.LENGTH_LONG)
                        .show()
                }
                else
                    createTimeDialog(selectedCalendar = calendar, movie = movie)
            },
            getCurrentDate().get(Calendar.YEAR),
            getCurrentDate().get(Calendar.MONTH),
            getCurrentDate().get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun createTimeDialog(selectedCalendar: Calendar, movie: Movie) {
        TimePickerDialog(
            requireContext(),
            // after accepting certain date
            { view: TimePicker?, hour: Int, minute: Int ->
                val calendar = Calendar.getInstance().apply {
                    set(
                        selectedCalendar.get(Calendar.YEAR),
                        selectedCalendar.get(Calendar.MONTH),
                        selectedCalendar.get(Calendar.DAY_OF_MONTH),
                        hour,
                        minute,
                        0
                    )
                }
                if (timePassed(date = calendar.time)) {
                    Toast.makeText(requireContext(), resources.getString(R.string.datePassed), Toast.LENGTH_LONG)
                        .show()
                }
                else
                    AlarmManagerController().execute(
                        context = WeakReference(requireContext()),
                        milliseconds = differenceBetweenDates(calendar.time),
                        movie = movie
                    )
            },
            getCurrentDate().get(Calendar.HOUR),
            getCurrentDate().get(Calendar.MINUTE),
            true
        ).show()
    }



    /** implementation of adapter's interface */
    override fun onMovieClicked(movie: Movie) {
        try {
            createDateDialog(movie = movie)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: RuntimeException) {
            Snackbar.make(requireView(), resources.getString(R.string.displayingError), Snackbar.LENGTH_LONG)
                .show()
            e.printStackTrace()
        }
    }
}