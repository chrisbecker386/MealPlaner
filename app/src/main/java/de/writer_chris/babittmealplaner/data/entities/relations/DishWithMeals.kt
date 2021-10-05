package de.writer_chris.babittmealplaner.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.entities.Meal

data class DishWithMeals(
    @Embedded val dish: Dish,
    @Relation(
        parentColumn = "dishId",
        entityColumn = "dishId")
    val meals: List<Meal>
)