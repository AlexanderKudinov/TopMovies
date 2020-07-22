package com.alexkudin.topmovies

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import com.alexkudin.topmovies.data.Movie
import com.alexkudin.topmovies.helpers.Constants.ACTION_SHOW_NOTIFICATION
import com.alexkudin.topmovies.helpers.Constants.EXTRA_MOVIE
import java.lang.ref.WeakReference

class AlarmManagerController {

    fun execute(context: WeakReference<Context>, milliseconds: Long, movie: Movie) {
        val intent = Intent(ACTION_SHOW_NOTIFICATION)
            .setPackage(context.get()?.packageName)
            .putExtra(EXTRA_MOVIE, movie)
        val pendingIntent = PendingIntent.getBroadcast(
            context.get(),
            movie.id.toInt(), // different request codes for each movie
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.get()?.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            RTC_WAKEUP,
            System.currentTimeMillis() + milliseconds,
            pendingIntent
        )
    }
}