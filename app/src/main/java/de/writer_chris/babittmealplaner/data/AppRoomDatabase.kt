package de.writer_chris.babittmealplaner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.writer_chris.babittmealplaner.data.entities.*

@Database(
    entities = [
        Dish::class,
        Ingredient::class,
        DishIngredientCrossRef::class,
        Meal::class,
        MealType::class,
        UnitType::class,
        Period::class
               ],
    version = 9,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun dishDao(): DishDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppRoomDatabase::class.java,
                        "meal_planer_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}