package de.writer_chris.babittmealplaner.ui.dish

import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.Repository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class DishViewModel(private val repository: Repository) : ViewModel() {

    val allDishes: LiveData<List<Dish>> = repository.getAllDishes().asLiveData()

    private fun insertDish(dish: Dish) {
        viewModelScope.launch { repository.insertDish(dish) }
    }

    private fun updateDish(dish: Dish) {
        viewModelScope.launch { repository.updateDish(dish) }
    }

    private fun deleteDish(dish: Dish) {
        viewModelScope.launch { repository.deleteDish(dish) }
    }

    fun retrieve(id: Int): LiveData<Dish> {
        return repository.getDish(id).asLiveData()
    }

    private fun getNewDishEntry(dishName: String): Dish {
        return Dish(dishName = dishName, description = "", duration = 0)
    }

    fun addDish(dishName: String) {
        val newDish = getNewDishEntry(dishName)
        insertDish(newDish)
    }

    fun editDish(dishId: Int, dishName: String) {
        val toUpdateDish: Dish = Dish(dishId, dishName, 0, "")
        updateDish(toUpdateDish)
    }

    fun eraseDish(dishId: Int) {
        val dish: Dish = Dish(dishId, "", 0, "")
        deleteDish(dish)
    }

    fun isEntryValid(value: String): Boolean {
        return value.isNotBlank()
    }
}

class DishViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DishViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }

}