package de.writer_chris.babittmealplaner.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository(context: Context) {
    private val dishDao: DishDao

    init {
        val db = AppRoomDatabase.getDatabase(context)
        dishDao = db.dishDao()
    }

    suspend fun insert(dish: Dish) {
        withContext(Dispatchers.IO) {
            dishDao.insert(dish)
        }
    }

    suspend fun update(dish: Dish) {
        withContext(Dispatchers.IO) {
            dishDao.update(dish)
        }
    }

    suspend fun delete(dish: Dish) {
        withContext(Dispatchers.IO) {
            dishDao.delete(dish)
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