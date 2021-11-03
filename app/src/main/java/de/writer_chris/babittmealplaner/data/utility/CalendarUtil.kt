package de.writer_chris.babittmealplaner.data.utility

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.util.Log
import de.writer_chris.babittmealplaner.R

import java.time.LocalDate
import android.icu.util.Calendar

class CalendarUtil {
    companion object {


        fun longToWeekday(number: Long): String {
            val cal = longToCalendar(number)
            return calendarToWeekday(cal)
        }

        fun longToGermanDate(number: Long): String {
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            return sdf.format(longToCalendar(number)).toString()
        }

        fun calendarToWeekday(cal: Calendar): String {
            val sdf = SimpleDateFormat("EEEE")
            return sdf.format(cal).toString()
        }


        private fun longToCalendar(number: Long): Calendar {
            val cal = Calendar.getInstance()
            cal.timeInMillis = number
            return cal
        }
    }
}
