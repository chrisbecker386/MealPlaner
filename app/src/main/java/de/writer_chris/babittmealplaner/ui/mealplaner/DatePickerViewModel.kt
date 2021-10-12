package de.writer_chris.babittmealplaner.ui.mealplaner

import android.icu.util.Calendar
import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Period
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class DatePickerViewModel(private val repository: Repository) : ViewModel() {

    private val _startDate = MutableLiveData<Calendar>(Calendar.getInstance())
    val startDate: LiveData<Calendar> = _startDate
    private val _endDate = MutableLiveData<Calendar>()
    val endDate: LiveData<Calendar> = _endDate

    init {
        _endDate.value = defaultEndDate()
    }

    private fun defaultEndDate(): Calendar {
        var cal = Calendar.getInstance()
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return cal
    }

    private fun insertPeriod(period: Period) {
        viewModelScope.launch { repository.insertPeriod(period) }
    }

    fun addPeriod() {
        val period = Period(
            startDate = startDate.value!!.let { CalendarUtil.calendarToLong(it) },
            endDate = endDate.value!!.let { CalendarUtil.calendarToLong(it) })
        insertPeriod(period)
    }

    fun setStartDate(calendar: Calendar) {
        _startDate.value = calendar
    }

    fun setEndDate(calendar: Calendar){
        _endDate.value = calendar
    }


}

class DatePickerViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatePickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatePickerViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")

    }
}
