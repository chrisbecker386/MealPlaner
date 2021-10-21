package de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.Period
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
        val start = it.first?.timeInMillis
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - endMillis null")
        val end = it.second?.timeInMillis
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - endMillis null")

        return@switchMap liveData { emit(getDayDiff(start, end)) }
    }

    init {
        _startDate.value = getCalendarDate(null, 0)
        _endDate.value = getCalendarDate(null, 6)
    }

    private fun getCalendarDate(timeInMillis: Long?, additionalDays: Int): Calendar {
        val cal = Calendar.getInstance()
        if (timeInMillis != null) {
            cal.timeInMillis = timeInMillis
        }
        cal.add(Calendar.DAY_OF_YEAR, additionalDays)
        return formatCalender(cal)
    }

    private fun insertPeriodAndItsMeals(period: Period) {
        val start = startDate.value
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - actualDate")
        val end = endDate.value
            ?: throw IllegalArgumentException("$LOG insertMealsForPeriod() - actualDate")

        val days = getDayDiff(start.timeInMillis, end.timeInMillis)
        viewModelScope.launch {
            val periodId = repository.insertPeriod(period)
            addMeals(start, days, periodId)
        }
    }

    private fun getDayDiff(lowerValue: Long, higherValue: Long): Int {
        val diffMillis = higherValue.minus(lowerValue)
        val cal = Calendar.getInstance()
        cal.timeInMillis = diffMillis
        return cal.get(Calendar.DAY_OF_YEAR)
    }

    private fun formatCalender(calendar: Calendar): Calendar {
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        return calendar
    }

    private fun addMeals(firstDayToInsert: Calendar, numberOfDays: Int, periodId: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = firstDayToInsert.timeInMillis
        viewModelScope.launch {
            repeat(numberOfDays) {
                dayDish.forEach {
                    repository.insertMeal(
                        Meal(
                            dishId = null, date = cal.timeInMillis,
                            mealType = it, periodId = periodId
                        )
                    )
                }
                cal.add(Calendar.DATE, 1)
            }
        }
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
        val start =
            startDate.value ?: throw IllegalArgumentException("startDate is null")
        val end = endDate.value ?: throw IllegalArgumentException("endDate is null")

        val updatedPeriod = Period(period.periodId, start.timeInMillis, end.timeInMillis)
        viewModelScope.launch {
            //update in period
            repository.updatePeriod(updatedPeriod)
            //removing overhang in meal
            repository.deleteMealsBeforeAndAfter(updatedPeriod)
            //adding new in meal
            insertPeriodAndItsMeals(updatedPeriod)
        }
    }

    fun deletePeriod(period: Period) {
        viewModelScope.launch {
            repository.deleteMealsFromPeriod(periodId = period.periodId)
            repository.deletePeriod(period)
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
