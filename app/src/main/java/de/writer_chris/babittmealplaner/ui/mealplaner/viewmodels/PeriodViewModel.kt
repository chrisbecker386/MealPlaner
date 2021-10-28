package de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels

import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import java.lang.IllegalArgumentException
import de.writer_chris.babittmealplaner.data.entities.Period


class PeriodViewModel(private val repository: Repository) : ViewModel() {
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