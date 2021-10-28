package de.writer_chris.babittmealplaner.ui.mealplaner.viewmodels

import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.relations.MealAndDish
import de.writer_chris.babittmealplaner.ui.mealplaner.models.DayMealsAndDish

class MealsFromPeriodViewModel(
    private val repository: Repository,
    periodId: Int
) :
    ViewModel() {

    private var _mealsAndDishes: LiveData<List<MealAndDish>> = retrieveMealsWithDishes(periodId)
    val mealsAndDishes get() = _mealsAndDishes

    private fun retrieveMealsWithDishes(periodId: Int): LiveData<List<MealAndDish>> {
        return repository.retrieveMealsWithDishes(periodId).asLiveData()
    }

    fun getDayMealsAndDish(): List<DayMealsAndDish> {
        val allMealsWithDish =
            mealsAndDishes.value ?: throw IllegalArgumentException("meals is null")
        val dayMealsAndDishes = mutableListOf<DayMealsAndDish>()

        lateinit var breakfast: MealAndDish
        lateinit var lunch: MealAndDish
        lateinit var dinner: MealAndDish
        var counter = 0
        for (i in allMealsWithDish) {
            when (counter) {
                0 -> breakfast = i
                1 -> lunch = i
                else -> dinner = i
            }
            if (counter >= 2) {

                dayMealsAndDishes.add(
                    DayMealsAndDish(
                        breakfast.meal.date,
                        breakfast,
                        lunch,
                        dinner
                    )
                )
                counter = 0
            } else {
                counter++
            }
        }
        return dayMealsAndDishes
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