package de.writer_chris.babittmealplaner.ui.mealplaner

import android.util.Log
import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import java.lang.IllegalArgumentException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import android.icu.util.Calendar


enum class typeOfMeal(val type: String) { BREAKFAST("breakfast"), LUNCH("lunch"), DINNER("dinner") }
class MealViewModel(private val repository: Repository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is meal Fragment"
    }
    val text: LiveData<String> = _text
    lateinit var mealSchedule: MutableList<List<MealWithDish>>

    init {
        mealSchedule = getScheduleWithDefaultData()
    }

    //generate a list for every day this and the next week without db data
    private fun getScheduleWithDefaultData(): MutableList<List<MealWithDish>> {
        val scheduleList = mutableListOf<List<MealWithDish>>()
        var date = getDateOfFirstAndLastDay()[0]
        repeat(14) {
            val tempDate = CalendarUtil.calendarToLong(date)
            val list: List<MealWithDish> = listOf(
                MealWithDish(tempDate, typeOfMeal.BREAKFAST.type),
                MealWithDish(tempDate, typeOfMeal.LUNCH.type),
                MealWithDish(tempDate, typeOfMeal.DINNER.type)
            )
            scheduleList.add(list)
            date.add(Calendar.DATE, 1)
        }
        return scheduleList
    }

    //gives the back the first date(monday) and the last day(sunday, 13 days later) of the shown schedule
    private fun getDateOfFirstAndLastDay(): Array<Calendar> {
        val local = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        Log.d("Monday", "${local.year}.${local.month}.${local.dayOfMonth} - ${local.dayOfWeek}")
        val start = CalendarUtil.localDateToCalendar(local)
        val end = CalendarUtil.localDateToCalendar(local.plusDays(13))
        return arrayOf(start, end)
    }

    private fun getScheduledMeals(): LiveData<List<Meal>> {
        val firstDay = CalendarUtil.calendarToLong(getDateOfFirstAndLastDay()[0])
        val lastDay = CalendarUtil.calendarToLong(getDateOfFirstAndLastDay()[1])
        return repository.getMealsOfThisWeek(firstDay, lastDay).asLiveData()
    }

}


class MealViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")

    }
}