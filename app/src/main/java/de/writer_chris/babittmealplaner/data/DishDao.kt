package de.writer_chris.babittmealplaner.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dish: Dish)

    @Update
    suspend fun update(dish: Dish)

    @Delete
    suspend fun delete(dish: Dish)

    @Query("SELECT * FROM dish")
    fun getAllDishes(): Flow<List<Dish>>

    @Query("SELECT * FROM DISH WHERE id=:dishId")
    fun getDish(dishId: Int): Flow<Dish>
}