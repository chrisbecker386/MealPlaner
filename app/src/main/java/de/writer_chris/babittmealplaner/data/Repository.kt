package de.writer_chris.babittmealplaner.data

import android.content.Context
import de.writer_chris.babittmealplaner.data.entities.Dish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository(context: Context) {
    private val dishDao: DishDao

    init {
        val db = AppRoomDatabase.getDatabase(context)
        dishDao = db.dishDao()
    }

    //Dish
    suspend fun insertDish(dish: Dish) {
        withContext(Dispatchers.IO) {
            dishDao.insertDish(dish)
        }
    }

    suspend fun updateDish(dish: Dish) {
        withContext(Dispatchers.IO) {
            dishDao.updateDish(dish)
        }
    }

    suspend fun deleteDish(dish: Dish) {
        withContext(Dispatchers.IO) {
            dishDao.deleteDish(dish)
        }
    }

    fun getAllDishes() = dishDao.getAllDishes()
    fun getDish(dishId: Int) = dishDao.getDish(dishId)
    suspend fun getDishWithoutFlow(dishName: String): Dish {
        var dish: Dish
        withContext(Dispatchers.IO) {
            dish = dishDao.getDishWithoutFlow(dishName)
        }
        return dish
    }



}