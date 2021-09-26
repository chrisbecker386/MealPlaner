package de.writer_chris.babittmealplaner.ui.dish

import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.Dish
import de.writer_chris.babittmealplaner.data.DishDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class DishViewModel(private val dishDao: DishDao) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dish Fragment"
    }
    val text: LiveData<String> = _text

    val allDishes: LiveData<List<Dish>> = dishDao.getAllDishes().asLiveData()

    private fun insertDish(dish: Dish) {
        viewModelScope.launch { dishDao.insert(dish) }
    }

    private fun updateDish(dish: Dish) {
        viewModelScope.launch { dishDao.update(dish) }
    }

    private fun deleteDish(dish: Dish) {
        viewModelScope.launch { dishDao.delete(dish) }
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

    fun retrieve(id: Int): LiveData<Dish> {
        return dishDao.getDish(id).asLiveData()
    }

}

class DishViewModelFactory(private val dishDao: DishDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishViewModel::class.java)) {
            @Suppress("UNCHECK_CAST")
            return DishViewModel(dishDao) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }

}