package de.writer_chris.babittmealplaner.data

import android.content.Context
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.Period
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

    fun getMealsFromPeriod(periodId: Int): Flow<List<Meal>> = dishDao.getMealsFromPeriod(periodId)

    fun getMealsOfDay(day: Long): Flow<List<Meal>> = dishDao.getMealsOfDay(day)


    //Period
    suspend fun insertPeriod(period: Period): Int {
        var periodId: Int
        withContext(Dispatchers.IO) {
            periodId = dishDao.insertPeriod(period).toInt()
        }
        return periodId
    }

    suspend fun updatePeriod(period: Period) {
        withContext(Dispatchers.IO) {
            dishDao.updatePeriod(period)
        }
    }

    suspend fun deletePeriod(period: Period) {
        withContext(Dispatchers.IO) {
            dishDao.deletePeriod(period)
        }
    }

    fun getAllPeriods(): Flow<List<Period>> = dishDao.getAllPeriods()

    fun getPeriod(periodId: Int): Flow<Period> = dishDao.getPeriod(periodId)

    suspend fun getPeriodId(startDate: Long, endDate: Long): Int {
        var res: Int
        withContext(Dispatchers.IO) {
            res = dishDao.getPeriodId(startDate, endDate)
        }
        return res
    }

    fun getPeriodIdFlow(startDate: Long, endDate: Long) =
        dishDao.getPeriodIdFlow(startDate, endDate)

    fun getLatestPeriodId() = dishDao.getLatestPeriodId()
}