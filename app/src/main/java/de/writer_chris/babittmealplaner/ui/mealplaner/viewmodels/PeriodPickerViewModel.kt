package de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.Period
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import kotlinx.coroutines.launch
import kotlin.IllegalArgumentException

const val LOG = "PeriodPickerViewModel"

class PeriodPickerViewModel(private val repository: Repository) : ViewModel() {

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
        _startDate.value = defaultStartDate()
        _endDate.value = defaultEndDate()
    }

    private fun defaultStartDate(): Calendar {
        val cal = Calendar.getInstance()
        return formatCalender(cal)
    }

    private fun defaultEndDate(): Calendar {
        val cal = Calendar.getInstance()

        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return formatCalender(cal)
    }

    fun setStartDate(calendar: Calendar) {
        val end =
            endDate.value ?: throw IllegalArgumentException("$LOG setStartDate - endDate is null")
        if (calendar.timeInMillis >= end.timeInMillis) {
            _endDate.value = calendar
        }
        _startDate.value = formatCalender(calendar)
    }

    fun setEndDate(calendar: Calendar) {
        val start =
            startDate.value ?: throw IllegalArgumentException("$LOG setEndDate - startDate is null")
        if (calendar.timeInMillis <= start.timeInMillis) {
            _startDate.value = calendar
        }
        _endDate.value = formatCalender(calendar)
    }


    fun retrievePeriod(id: Int): LiveData<Period> {
        return repository.getPeriod(id).asLiveData()
    }

    private fun insertPeriodAndItsMeals(period: Period) {
        val actualDate = startDate.value
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - actualDate")
        val days = getDayCountStartEnd()

        viewModelScope.launch {
            val periodId = repository.insertPeriod(period)
            addMultipleMeals(actualDate, days, periodId)

        }
    }


    private fun getDayCountStartEnd(): Int {
        val startMillis = startDate.value?.timeInMillis
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - actualDate")
        val endMillis = endDate.value?.timeInMillis
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - endMillis null")

        val diffMillis = endMillis.minus(startMillis)
        val cal = Calendar.getInstance()
        cal.timeInMillis = diffMillis

        return cal.get(Calendar.DAY_OF_YEAR)
    }

    private fun getDayDiff(a: Long, b: Long): Int {
        val diffMillis = b.minus(a)
        val cal = Calendar.getInstance()
        cal.timeInMillis = diffMillis
        return (cal.get(Calendar.DAY_OF_YEAR) - 1)
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


    fun updatePeriod(period: Period) {
        //TODO update period ->check
        //TODO delete before and after new time -> check
        //TODO add dayMeals for the other days
        val start =
            startDate.value ?: throw IllegalArgumentException("startDate is null")
        val end = endDate.value ?: throw IllegalArgumentException("endDate is null")

        val updatedPeriod = Period(
            periodId = period.periodId,
            startDate = start.timeInMillis,
            endDate = end.timeInMillis
        )
        viewModelScope.launch {
            //update in period
            repository.updatePeriod(updatedPeriod)
            //removing overhang in meal
            repository.deleteMealsBeforeAndAfter(updatedPeriod)

            val latest = repository.getLatestMealInPeriod(updatedPeriod.periodId)
            if (latest != end.timeInMillis) {
                val days = getDayDiff(latest, end.timeInMillis)
                val cal = Calendar.getInstance()
                cal.timeInMillis = latest
                cal.add(Calendar.DATE, 1)
                addMultipleMeals(cal, days, updatedPeriod.periodId)
            }

            //adding days on gaps on the beginning
            val earliest = repository.getEarliestMealInPeriod(updatedPeriod.periodId)
            if (earliest != start.timeInMillis) {
                val days = getDayDiff(start.timeInMillis, earliest)
                addMultipleMeals(start, days, updatedPeriod.periodId)
            }
        }
    }

    fun deletePeriod(period: Period) {
        viewModelScope.launch {
            repository.deleteMealsFromPeriod(periodId = period.periodId)
            repository.deletePeriod(period)

        }
    }

    private fun formatCalender(calendar: Calendar): Calendar {
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        return calendar
    }

    private fun addMultipleMeals(firstDayToInsert: Calendar, numberOfDays: Int, periodId: Int) {
        viewModelScope.launch {
            repeat(numberOfDays) {
                dayDish.forEach {
                    repository.insertMeal(
                        Meal(
                            dishId = null, date = firstDayToInsert.timeInMillis,
                            mealType = it, periodId = periodId
                        )
                    )
                }
                firstDayToInsert.add(Calendar.DATE, 1)
            }
        }
    }

}

class PeriodPickerViewModelFactory(
    private val repository: Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PeriodPickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PeriodPickerViewModel(repository) as T
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
