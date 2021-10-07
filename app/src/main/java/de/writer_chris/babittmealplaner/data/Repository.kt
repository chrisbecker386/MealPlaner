package de.writer_chris.babittmealplaner.data

import android.content.Context
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.entities.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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

    //Meal
    suspend fun insertMeal(meal: Meal) {
        withContext(Dispatchers.IO) {
            dishDao.insertMeal(meal)
        }
    }

    suspend fun updateMeal(meal: Meal) {
        withContext(Dispatchers.IO) {
            dishDao.updateMeal(meal)
        }
    }

    suspend fun deleteMeal(meal: Meal) {
        withContext(Dispatchers.IO) {
            dishDao.deleteMeal(meal)
        }
    }

    fun getAllMeals(): Flow<List<Meal>> = dishDao.getAllMeals()

    fun getMealsOfDay(day: Long): Flow<List<Meal>> = dishDao.getMealsOfDay(day)

    fun getMealsOfThisWeek(startOfWeek: Long, endOfWeek: Long): Flow<List<Meal>> =
        dishDao.getMealsOfThisWeek(startOfWeek, endOfWeek)

    //
}