package com.alexkudin.topmovies

import com.alexkudin.topmovies.helpers.DateController.datePassed
import com.alexkudin.topmovies.helpers.DateController.parseSecondaryDateFromPrimary
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

// only simple tests
@RunWith(JUnit4::class)
class DateControllerUnitTest {

    @Test
    fun testDateParsing() {
        Assertions.assertThat(parseSecondaryDateFromPrimary("2019-12-12")).isEqualTo("December 12, 2019")
        Assertions.assertThat(parseSecondaryDateFromPrimary("2019-01-30")).isEqualTo("January 30, 2019")
    }

    @Test
    fun testDatePassed() {
        val calendar1 = Calendar.getInstance().apply {
            set(2001, 10, 20)
        }
        val calendar2 = Calendar.getInstance().apply {
            set(2021, 10, 20)
        }
        val calendar3 = Calendar.getInstance().apply {
            set(2001, 1, 1)
        }
        val calendar4 = Calendar.getInstance().apply {
            set(2001, 12, 1)
        }
        val calendar5 = Calendar.getInstance().apply {
            set(2001, 1, 30)
        }

        Assertions.assertThat(datePassed(calendar1)).isTrue()
        Assertions.assertThat(datePassed(calendar3)).isTrue()
        Assertions.assertThat(datePassed(calendar4)).isTrue()
        Assertions.assertThat(datePassed(calendar5)).isTrue()
        Assertions.assertThat(datePassed(calendar2)).isFalse()
    }
}