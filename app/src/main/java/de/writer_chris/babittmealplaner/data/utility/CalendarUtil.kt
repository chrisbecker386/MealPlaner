package de.writer_chris.babittmealplaner.data.utility

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat

import android.icu.util.Calendar

class CalendarUtil {
    companion object {
        fun longToWeekday(timeInMillis: Long): String {
            val cal = longToCalendar(timeInMillis)
            return calendarToWeekday(cal)
        }

        fun longToDate(timeInMillis: Long): String {
            val cal = longToCalendar(timeInMillis)
            return DateFormat.getDateInstance(DateFormat.SHORT).format(cal)
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
