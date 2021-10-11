package de.writer_chris.babittmealplaner.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.Period

data class PeriodWithMeals(
    @Embedded val period: Period,
    @Relation(
        parentColumn = "periodId",
        entityColumn = "mealId"
    )
    val meals: List<Meal>
)