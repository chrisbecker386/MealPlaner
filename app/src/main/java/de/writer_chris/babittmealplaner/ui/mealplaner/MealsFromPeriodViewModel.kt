package de.writer_chris.babittmealplaner.ui.mealplaner

import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.ui.mealplaner.model.DayMeals

class MealsFromPeriodViewModel(
    private val repository: Repository,
    periodId: Int
) :
    ViewModel() {

    private var _meals: LiveData<List<Meal>> = retrieveMeals(periodId)
    val meals get() = _meals

    private fun retrieveMeals(periodId: Int): LiveData<List<Meal>> {
        return repository.getMealsFromPeriod(periodId).asLiveData()
    }

    fun getDayMeals(): List<DayMeals> {
        val allMeals = meals.value ?: throw IllegalArgumentException("meals is null")
        val dayMeals = mutableListOf<DayMeals>()

        lateinit var breakfast: Meal
        lateinit var lunch: Meal
        lateinit var dinner: Meal
        var counter = 0

        for (i in allMeals) {
            when (counter) {
                0 -> breakfast = i
                1 -> lunch = i
                else -> dinner = i
            }
            if (counter >= 2) {

                dayMeals.add(DayMeals(breakfast.date, breakfast, lunch, dinner))
                counter = 0
            } else {
                counter++
            }
        }
        return dayMeals
    }
}

class MealsFromPeriodViewModelFactory(
    private val repository: Repository,
    private val periodId: Int
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealsFromPeriodViewModel::class.java)) {
            @Suppress("UNCHECKED CAST")
            return MealsFromPeriodViewModel(repository, periodId) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}