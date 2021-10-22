package de.writer_chris.babittmealplaner.ui.dish

import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.entities.Meal
import kotlinx.coroutines.launch

class DishDetailsViewModel(private val repository: Repository) : ViewModel() {

    fun retrieveDish(dishId: Int): LiveData<Dish> {
        return repository.getDish(dishId).asLiveData()
    }

    fun retrieveMeal(mealId: Int): LiveData<Meal> {
        return repository.getMeal(mealId).asLiveData()
    }

    fun updateMealWithDishId(meal: Meal, dish: Dish) {
        val updatedMeal = Meal(
            mealId = meal.mealId, dishId = dish.dishId,
            date = meal.date, mealType = meal.mealType,
            periodId = meal.periodId
        )
        viewModelScope.launch {
            repository.updateMeal(updatedMeal)
        }
    }
}

class DishDetailsViewModelFactory(
    private val repository: Repository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DishDetailsViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }
}