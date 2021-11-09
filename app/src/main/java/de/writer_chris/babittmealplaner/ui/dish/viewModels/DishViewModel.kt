package de.writer_chris.babittmealplaner.ui.dish.viewModels

import android.content.Context
import androidx.lifecycle.*
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.data.utility.TEMPORAL_FILE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class DishViewModel(private val repository: Repository) : ViewModel() {
    val allDishes: LiveData<List<Dish>> = repository.getAllDishes().asLiveData()
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