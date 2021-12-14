package de.writer_chris.babittmealplaner.data

import androidx.room.*
import de.writer_chris.babittmealplaner.data.entities.*
import de.writer_chris.babittmealplaner.data.entities.relations.DishWithMeals
import de.writer_chris.babittmealplaner.data.entities.relations.MealAndDish
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    //Dish table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDish(dish: Dish):Long

    @Update
    suspend fun updateDish(dish: Dish)

    @Delete
    suspend fun deleteDish(dish: Dish)

    @Transaction
    @Query("SELECT * FROM dish ORDER BY dish_name DESC")
    fun getAllDishes(): Flow<List<Dish>>

    @Transaction
    @Query("SELECT * FROM DISH WHERE dishId=:dishId")
    fun getDish(dishId: Int): Flow<Dish>

    @Transaction
    @Query("SELECT * FROM DISH WHERE dish_name=:dishName")
    suspend fun getDishWithoutFlow(dishName: String): Dish

    //Meal table
    @Transaction
    @Query("INSERT INTO Meal (date, mealType, periodId)  SELECT :date, :mealType, :periodId WHERE NOT EXISTS (SELECT date, mealType, periodID FROM Meal WHERE date=:date AND mealType=:mealType AND periodId=:periodId)")
    suspend fun insertMeal(date: Long, mealType: String, periodId: Int)

    @Transaction
    @Update
    suspend fun updateMeal(meal: Meal)

    @Transaction
    @Query("SELECT * FROM Meal WHERE mealId =:mealId")
    fun getMeal(mealId: Int): Flow<Meal>

    @Transaction
    @Query("DELETE FROM Meal WHERE periodId=:periodId")
    fun deleteMealsFromPeriod(periodId: Int)

    @Transaction
    @Query("DELETE FROM Meal WHERE periodId =:periodId AND (date<:startDate OR date>:endDate)")
    fun deleteMealsBeforeAndAfter(periodId: Int, startDate: Long, endDate: Long)

    //Period table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeriod(period: Period): Long

    @Update
    suspend fun updatePeriod(period: Period)

    @Delete
    suspend fun deletePeriod(period: Period)

    @Transaction
    @Query("SELECT * FROM Period")
    fun getAllPeriods(): Flow<List<Period>>

    @Transaction
    @Query("SELECT * FROM Period WHERE periodId =:periodId")
    fun getPeriod(periodId: Int): Flow<Period>

    //MealAndDish
    @Transaction
    @Query("SELECT * FROM Meal LEFT JOIN DISH ON Meal.dishId =Dish.dishId WHERE periodId =:periodId ORDER BY date ASC")
    fun retrieveMealsWithDishes(periodId: Int): Flow<List<MealAndDish>>
}
