package de.writer_chris.babittmealplaner.data.utility

import android.util.Log
import de.writer_chris.babittmealplaner.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import android.icu.util.Calendar

class CalendarUtil {
    companion object {
        fun localDateToCalendar(localDate: LocalDate): Calendar {
            val cal = Calendar.getInstance()
            cal.set(localDate.year, (localDate.monthValue-1), localDate.dayOfMonth, 0, 0, 0)
            val sdf = SimpleDateFormat("EEE yyyy-MM-dd ")
//            Log.d("Date", "${sdf.format(cal.time)}")

            return cal
        }

        fun calendarToLong(cal: Calendar): Long {
            return cal.timeInMillis
        }

        fun longToCalendar(number: Long): Calendar {
            val cal = Calendar.getInstance()
            cal.timeInMillis = number
            return cal
        }

        fun longToWeekdayResId(number: Long): Int {
            val cal = longToCalendar(number)


            val weekday: Int = when (cal.get(Calendar.DAY_OF_WEEK)) {
                2 -> R.string.monday
                3 -> R.string.tuesday
                4 -> R.string.wednesday
                5 -> R.string.thursday
                6 -> R.string.friday
                7 -> R.string.saturday
                else -> R.string.sunday
            }
            return weekday
        }
        fun longToGermanDate(number: Long):String{
            val sdf = SimpleDateFormat("dd.MM.yyyy")
            return sdf.format(longToCalendar(number).time)
        }
    }
}