package de.writer_chris.babittmealplaner.data

import android.content.Context
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.Period
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow


class Repository(context: Context) {
    private val dishDao: DishDao

    init {
        val db = AppRoomDatabase.getDatabase(context)
        dishDao = db.dishDao()
    }

    //Dish
    suspend fun insertDish(dish: Dish): Int {
        var dishId: Int
        withContext(Dispatchers.IO) {
            dishId = dishDao.insertDish(dish).toInt()
        }
        return dishId
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
        val date: Long = meal.date
        val mealType: String = meal.mealType
        val periodId: Int = meal.periodId

        withContext(Dispatchers.IO) {
            dishDao.insertMeal(date, mealType, periodId)
        }
    }

    suspend fun updateMeal(meal: Meal) {
        withContext(Dispatchers.IO) {
            dishDao.updateMeal(meal)
        }
    }

    fun getMeal(mealId: Int): Flow<Meal> = dishDao.getMeal(mealId)

    suspend fun deleteMealsFromPeriod(periodId: Int) {
        withContext(Dispatchers.IO) {
            dishDao.deleteMealsFromPeriod(periodId)
        }
    }

    suspend fun deleteMealsBeforeAndAfter(period: Period) {
        val periodId = period.periodId
        val startDate = period.startDate
        val endDate = period.endDate
        withContext(Dispatchers.IO) {
            dishDao.deleteMealsBeforeAndAfter(periodId, startDate, endDate)
        }
    }

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

    fun retrieveMealsWithDishes(periodId: Int) = dishDao.retrieveMealsWithDishes(periodId)


}