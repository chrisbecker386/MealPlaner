package de.writer_chris.babittmealplaner.ui.mealplaner

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.Period
import kotlinx.coroutines.launch
import kotlin.IllegalArgumentException

const val LOG = "PeriodPickerViewModel"

class PeriodPickerViewModel(
    private val repository: Repository,
    private val initStartDate: Calendar?,
    private val initEndDate: Calendar?
) : ViewModel() {

    private var _startDate = MutableLiveData<Calendar>()
    val startDate: LiveData<Calendar> get() = _startDate
    private var _endDate = MutableLiveData<Calendar>()
    val endDate: LiveData<Calendar> get() = _endDate
    private val dayDish = listOf("breakfast", "lunch", "dinner")

    init {
        if (initStartDate == null) {
            _startDate.value = defaultStartDate()
        } else {
            _startDate.value = initStartDate
        }
        if (initEndDate == null) {
            _endDate.value = defaultEndDate()
        } else {
            _endDate.value = initEndDate
        }

    }


    private fun defaultStartDate(): Calendar {
        return Calendar.getInstance()
    }

    private fun defaultEndDate(): Calendar {
        val cal = Calendar.getInstance()
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        Log.d(LOG, "${_startDate.value?.timeInMillis} ${cal.timeInMillis}")
        return cal
    }

    private fun insertPeriodAndItsMeals(period: Period) {
        val actualDate =
            startDate.value
                ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - actualDate")
        val days = getDaysBetweenStartAndEnd()

        viewModelScope.launch {
            //insert new entry to period table
            val periodId = repository.insertPeriod(period)

            //insert new entries to meal table
            repeat(days) {
                dayDish.forEach {
                    repository.insertMeal(
                        Meal(
                            dishId = null,
                            date = actualDate.timeInMillis,
                            mealType = it,
                            periodId = periodId
                        )
                    )
                }
                actualDate.add(Calendar.DATE, 1)
            }
        }
    }

    private fun getDaysBetweenStartAndEnd(): Int {
        val startMillis = startDate.value?.timeInMillis
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - actualDate")
        val endMillis = endDate.value?.timeInMillis
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - endMillis null")

        val diffMillis = endMillis.minus(startMillis)
        val cal = Calendar.getInstance()
        cal.timeInMillis = diffMillis

        return cal.get(Calendar.DAY_OF_YEAR)
    }


    fun addPeriod() {
        val start: Long =
            startDate.value?.timeInMillis
                ?: throw IllegalArgumentException("$LOG addPeriod() - start")
        val end: Long =
            endDate.value?.timeInMillis ?: throw IllegalArgumentException("$LOG addPeriod() - end")
        val period = Period(
            startDate = start,
            endDate = end
        )
        insertPeriodAndItsMeals(period)
    }

    fun setStartDate(calendar: Calendar) {
        _startDate.value = calendar
    }

    fun setEndDate(calendar: Calendar) {
        _endDate.value = calendar
    }
}

class PeriodPickerViewModelFactory(
    private val repository: Repository, private val initStartDate: Calendar?,
    private val initEndDate: Calendar?
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeriodPickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeriodPickerViewModel(repository, initStartDate, initEndDate) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")

    }
}

