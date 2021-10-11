package de.writer_chris.babittmealplaner.ui.mealplaner

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.writer_chris.babittmealplaner.data.Repository
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
