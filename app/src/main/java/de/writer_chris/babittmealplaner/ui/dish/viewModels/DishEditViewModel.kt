package de.writer_chris.babittmealplaner.ui.dish.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.data.utility.TEMPORAL_FILE_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class DishEditViewModel(private val repository: Repository):ViewModel(){

    private fun insertDish(dish: Dish, context: Context) {
        CoroutineScope(IO).launch {
            val dishId = repository.insertDish(dish)
            val bitmap = DataUtil.loadDishPictureFromInternalStorage(context, TEMPORAL_FILE_NAME)
            if (bitmap != null) {
                DataUtil.saveDishPictureToInternalStorage(
                    context, dishId.toString(),
                    bitmap
                )
            }
            DataUtil.deletePhotoFromInternalStorage(context, TEMPORAL_FILE_NAME)
        }
    }

    private fun updateDish(dish: Dish, context: Context) {
        CoroutineScope(IO).launch {
            repository.updateDish(dish)
            val bitmap = DataUtil.loadDishPictureFromInternalStorage(context, TEMPORAL_FILE_NAME)
            if (bitmap != null) {
                DataUtil.saveDishPictureToInternalStorage(
                    context, dish.dishId.toString(),
                    bitmap
                )
            }
            DataUtil.deletePhotoFromInternalStorage(context, TEMPORAL_FILE_NAME)
        }
    }

    private fun deleteDish(dish: Dish, context: Context) {
        CoroutineScope(IO).launch {
            repository.deleteDish(dish)
            DataUtil.deletePhotoFromInternalStorage(context, dish.dishId.toString())
        }
    }

    fun retrieve(id: Int): LiveData<Dish> {
        return repository.getDish(id).asLiveData()
    }

    private fun getNewDishEntry(
        dishName: String,
        description: String?,
        duration: Long?
    ): Dish {
        val des = description ?: ""
        val dur = duration ?: 0

        return Dish(dishName = dishName, description = des, duration = dur)
    }

    fun addDish(dishName: String, description: String?, duration: Long?, context: Context) {
        val newDish = getNewDishEntry(dishName, description, duration)
        insertDish(newDish, context)
    }

    fun editDish(dish: Dish, context: Context) {
        updateDish(dish, context)
    }

    fun eraseDish(dishId: Int, context: Context) {
        val dish: Dish = Dish(dishId, "", 0, "")
        deleteDish(dish, context)
    }

    fun isEntryValidString(value: String?): Boolean {
        return !(value == null || value.isNullOrEmpty())
    }

}


class DishEditViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DishEditViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DishEditViewModel(repository) as T
        }
        throw  IllegalArgumentException("Unknown ViewModel class")
    }

}