package de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels

import android.icu.util.Calendar
import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.Period
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil.Companion.getCalendarDate
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil.Companion.getNormalizedCalender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
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

    //communication methods
    fun retrievePeriod(id: Int): LiveData<Period> {
        return repository.getPeriod(id).asLiveData()
    }

    fun setStartDate(calendar: Calendar) {
        setStart(calendar)
    }

    fun setEndDate(calendar: Calendar) {
        setEnd(calendar)
    }

    fun addPeriod() {
        createPeriod()
    }

    fun updatePeriod(period: Period) {
        applyChangesPeriod(period)
    }

    fun deletePeriod(period: Period) {
        erasePeriod(period)
    }

    //database execution
    private fun createPeriod() {
        val start: Long = getStart().timeInMillis
        val end: Long = getEnd().timeInMillis
        val period = Period(startDate = start, endDate = end)
        insertPeriodAndItsMeals(period)
    }

    private fun insertPeriodAndItsMeals(period: Period) {
        val start = getStart()
        val days = getDaysBetween()

        CoroutineScope(IO).launch {
            val res = repository.insertPeriod(period)
            insertMeals(start, days, res)
        }
    }

    private fun applyChangesPeriod(period: Period) {
        val start = getStart()
        val end = getEnd()
        val days = getDaysBetween()
        val updatedPeriod = Period(period.periodId, start.timeInMillis, end.timeInMillis)

        CoroutineScope(IO).launch {
            //update in period
            repository.updatePeriod(updatedPeriod)
            //removing overhang in meal
            repository.deleteMealsBeforeAndAfter(updatedPeriod)
            //adding new in meal
            insertMeals(start, days, period.periodId)
        }
    }

    private fun erasePeriod(period: Period) {
        CoroutineScope(IO).launch {
            repository.deletePeriod(period)
            repository.deleteMealsFromPeriod(periodId = period.periodId)
        }
    }

    private suspend fun insertMeals(firstDayToInsert: Calendar, numberOfDays: Int, periodId: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = firstDayToInsert.timeInMillis

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

    //helper methods
    private fun setStart(calendar: Calendar) {
        val end = getEnd()
        if (calendar.timeInMillis >= end.timeInMillis) {
            _endDate.value = calendar
        }
        _startDate.value = getNormalizedCalender(calendar)
    }

    private fun setEnd(calendar: Calendar) {
        val start = getStart()
        if (calendar.timeInMillis <= start.timeInMillis) {
            _startDate.value = calendar
        }
        _endDate.value = getNormalizedCalender(calendar)
    }

    private fun getDayDiff(lowerValue: Long, higherValue: Long): Int {
        val diffMillis = higherValue.minus(lowerValue)
        val cal = Calendar.getInstance()
        cal.timeInMillis = diffMillis
        return cal.get(Calendar.DAY_OF_YEAR)
    }


    fun getStart(): Calendar {
        return startDate.value ?: throw IllegalArgumentException("startDate value null")
    }

    fun getEnd(): Calendar {
        return endDate.value ?: throw IllegalArgumentException("endDate value null")
    }

    private fun getDaysBetween(): Int {
        return daysBetween.value
            ?: throw IllegalArgumentException("daysBetween value null")
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
