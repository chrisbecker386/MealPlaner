package de.writer_chris.babittmealplaner.data

import androidx.room.*
import de.writer_chris.babittmealplaner.data.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    //Dish table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDish(dish: Dish)

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
    @Insert
    suspend fun insertMeal(meal: Meal)

    @Update
    suspend fun updateMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Transaction
    @Query("SELECT * FROM Meal")
    fun getAllMeals(): Flow<List<Meal>>

    @Transaction
    @Query("SELECT * FROM Meal WHERE date =:day")
    fun getMealsOfDay(day: Long): Flow<List<Meal>>

    @Transaction
    @Query("SELECT * FROM Meal WHERE date BETWEEN :startOfWeek AND :endOfWeek")
    fun getMealsOfThisWeek(startOfWeek: Long, endOfWeek: Long): Flow<List<Meal>>

    //MealType table
    @Insert
    suspend fun insertMealType(mealType: MealType)

    @Update
    suspend fun updateMealType(mealType: MealType)

    @Transaction
    @Query("SELECT * FROM MealType")
    fun getAllMealTypes(): Flow<List<MealType>>

    //Ingredient table
    @Insert
    suspend fun insertIngredient(ingredient: Ingredient)

    @Update
    suspend fun updateIngredient(ingredient: Ingredient)

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient)

    @Transaction
    @Query("SELECT * FROM ingredient")
    fun getAllIngredients(): Flow<List<Ingredient>>

    @Transaction
    @Query("SELECT * FROM ingredient WHERE ingredientId =:ingredientId")
    fun getIngredient(ingredientId: Int): Flow<Ingredient>

    //UnitType table
    @Insert
    suspend fun insertUnitType(unitType: UnitType)

    @Update
    fun updateUnitType(unitType: UnitType)

    @Transaction
    @Query("SELECT * FROM UnitType")
    fun getAllUnitTypes(): Flow<List<UnitType>>

    @Transaction
    @Query("SELECT * FROM UnitType WHERE unitType=:unitType")
    fun getUnitType(unitType: String): Flow<UnitType>

    //Period table
    @Insert
    suspend fun insertPeriod(period: Period)

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
}