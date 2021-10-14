package de.writer_chris.babittmealplaner.ui.mealplaner

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Meal
import java.lang.IllegalArgumentException

class MealsFromPeriodViewModel(private val repository: Repository) : ViewModel() {

    //this is wrong it needs specified meals from the db,
    // but for the first test is should be okay
    val meals: LiveData<List<Meal>> = repository.getAllMeals().asLiveData()
}

class MealsFromPeriodViewModelFactory(private val repository: Repository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealsFromPeriodViewModel::class.java)) {
            @Suppress("UNCHECKED CAST")
            return MealsFromPeriodViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}