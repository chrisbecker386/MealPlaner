package de.writer_chris.babittmealplaner.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.MealType

data class MealAndMealType(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "mealType",
        entityColumn = "mealType"
    )
    val mealType: MealType
)