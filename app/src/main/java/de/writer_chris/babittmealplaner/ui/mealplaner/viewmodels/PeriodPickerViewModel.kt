package de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels

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

    val daysBetween = PairMediatorLiveData(_startDate, _endDate).switchMap {
        val startMillis = it.first?.timeInMillis
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - endMillis null")
        val endMillis = it.second?.timeInMillis
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - endMillis null")

        val diffMillis = endMillis.minus(startMillis)
        val cal = Calendar.getInstance()
        cal.timeInMillis = diffMillis

        return@switchMap liveData { emit(cal.get(Calendar.DAY_OF_YEAR)) }
    }


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


    private fun getDaysBetweenLive(): LiveData<Int> {
        return liveData<Int> { emit(getDaysBetweenStartAndEnd()) }
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
        val end =
            endDate.value ?: throw IllegalArgumentException("$LOG setStartDate - endDate is null")
        if (calendar.timeInMillis >= end.timeInMillis) {
            _endDate.value = calendar
        }
        _startDate.value = calendar

    }

    fun setEndDate(calendar: Calendar) {
        val start =
            startDate.value ?: throw IllegalArgumentException("$LOG setEndDate - startDate is null")
        if (calendar.timeInMillis <= start.timeInMillis) {
            _startDate.value = calendar
        }
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

class PairMediatorLiveData<F, S>(firstLiveData: LiveData<F>, secondLiveData: LiveData<S>) :
    MediatorLiveData<Pair<F?, S?>>() {
    init {
        addSource(firstLiveData) { firstLiveDataValue: F ->
            value = firstLiveDataValue to secondLiveData.value
        }
        addSource(secondLiveData) { secondLiveDataValue: S ->
            value = firstLiveData.value to secondLiveDataValue
        }
    }
}
