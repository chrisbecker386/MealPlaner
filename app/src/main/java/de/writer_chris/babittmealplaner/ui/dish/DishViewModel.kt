package de.writer_chris.babittmealplaner.ui.dish

import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Dish
import de.writer_chris.babittmealplaner.data.DishDao
import de.writer_chris.babittmealplaner.data.Repository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class DishViewModel(private val repository: Repository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dish Fragment"
    }
    val text: LiveData<String> = _text

    val allDishes: LiveData<List<Dish>> = repository.getAllDishes().asLiveData()

    private fun insertDish(dish: Dish) {
        viewModelScope.launch { repository.insert(dish) }
    }

    private fun updateDish(dish: Dish) {
        viewModelScope.launch { repository.update(dish) }
    }

    private fun deleteDish(dish: Dish) {
        viewModelScope.launch { repository.delete(dish) }
    }

    private fun getNewDishEntry(dishName: String): Dish {
        return Dish(dishName = dishName)
    }

    fun addDish(dishName: String) {
        val newDish = getNewDishEntry(dishName)
        insertDish(newDish)
    }

    fun editDish(dishId: Int, dishName: String) {
        val toUpdateDish: Dish = Dish(dishId, dishName)
        updateDish(toUpdateDish)
    }

    fun eraseDish(dishId: Int){
        val dish:Dish = Dish(dishId, "")
        deleteDish(dish)
    }

    fun retrieve(id: Int): LiveData<Dish> {
        return repository.getDish(id).asLiveData()
    }

    fun isEntryValid(value:String):Boolean{
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