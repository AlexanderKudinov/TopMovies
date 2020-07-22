package com.alexkudin.topmovies.helpers

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateController {

    // convert date from received string from api to displayed string
    fun parseSecondaryDateFromPrimary(primary: String): String =
        try {
            val oldFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val newFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
            newFormat.format(oldFormat.parse(primary))
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    fun getSecondaryDate(secondary: String): Int =
        try {
            val format = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
            val calendar = Calendar.getInstance()
            calendar.time = format.parse(secondary)
            calendar.get(Calendar.YEAR)
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }

    fun getCurrentDate() = Calendar.getInstance().apply { time = Date() }

    fun timePassed(date: Date) = date.time < getCurrentDate().time.time

    fun datePassed(calendar: Calendar): Boolean {
        try {
            val yearPassed = calendar.get(Calendar.YEAR) < getCurrentDate().get(Calendar.YEAR)
            val yearEquals = calendar.get(Calendar.YEAR) == getCurrentDate().get(Calendar.YEAR)
            if (yearPassed) return true
            if (!yearEquals) return false
            val monthPassed = calendar.get(Calendar.MONTH) < getCurrentDate().get(Calendar.MONTH)
            val monthEquals = calendar.get(Calendar.MONTH) == getCurrentDate().get(Calendar.MONTH)
            if (monthPassed) return true
            if (!monthEquals) return false
            return calendar.get(Calendar.DAY_OF_MONTH) < getCurrentDate().get(Calendar.DAY_OF_MONTH)
        } catch (e: RuntimeException) {
            e.printStackTrace()
            return false
        }
    }

    // difference in milliseconds
    fun differenceBetweenDates(date: Date): Long {
        if (timePassed(date)) return 0
        return date.time - Date().time
    }
}
