package de.writer_chris.babittmealplaner.data.utility

import android.icu.text.DateFormat
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar

class CalendarUtil {
    companion object {
        private val sdf = SimpleDateFormat("EEEE")

        fun longToWeekday(timeInMillis: Long) = calendarToWeekday(longToCalendar(timeInMillis))

        fun longToDate(timeInMillis: Long): String {
            val cal = longToCalendar(timeInMillis)
            return DateFormat.getDateInstance(DateFormat.SHORT).format(cal)
        }

        fun calendarToWeekday(cal: Calendar): String {
            return sdf.format(cal).toString()
        }

        private fun longToCalendar(number: Long): Calendar {
            val cal = Calendar.getInstance()
            cal.timeInMillis = number
            return cal
        }

        fun getCalendarDate(timeInMillis: Long?, additionalDays: Int): Calendar {
            val cal = Calendar.getInstance()
            if (timeInMillis != null) {
                cal.timeInMillis = timeInMillis
            }
            cal.add(Calendar.DAY_OF_YEAR, additionalDays)
            return getNormalizedCalender(cal)
        }

         fun getNormalizedCalender(calendar: Calendar): Calendar {
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            return calendar
        }
    }
}
