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
import de.writer_chris.babittmealplaner.data.entities.Period


enum class typeOfMeal(val type: String) { BREAKFAST("breakfast"), LUNCH("lunch"), DINNER("dinner") }
class PeriodViewModel(private val repository: Repository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is meal Fragment"
    }
    val text: LiveData<String> = _text

    val periods: LiveData<List<Period>> = repository.getAllPeriods().asLiveData()
}


class PeriodViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeriodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeriodViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")

    }
}