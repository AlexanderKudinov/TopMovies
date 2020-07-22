package com.alexkudin.topmovies

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.alexkudin.topmovies.data.Movie
import com.alexkudin.topmovies.helpers.Constants.EXTRA_MOVIE
import com.alexkudin.topmovies.helpers.Constants.NOTIFICATION_CHANNEL_ID
import com.alexkudin.topmovies.helpers.Constants.NOTIFICATION_CHANNEL_NAME
import com.alexkudin.topmovies.helpers.Constants.REQUEST_MAIN_ACTIVITY

class NotificationReceiver: BroadcastReceiver() {

    private var notificationManager: NotificationManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.hasExtra(EXTRA_MOVIE) == true && context != null) {
            val movie: Movie = intent.getParcelableExtra(EXTRA_MOVIE)!!
            notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            createNotification(context = context, movie = movie)
            createNotificationChannel(context = context)
        }
    }

    private fun createNotification(context: Context, movie: Movie) {
        val pendingIntentMain = PendingIntent.getActivity(
            context,
            REQUEST_MAIN_ACTIVITY,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_ONE_SHOT
        )
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(context.resources.getString(R.string.timeToWatch))
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(movie.title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntentMain)
            .build()

        notificationManager?.notify(movie.id.toInt(), notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = context.resources.getString(R.string.mainNotificationChannel)
                enableVibration(true)
                enableLights(true)
            }
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }
}