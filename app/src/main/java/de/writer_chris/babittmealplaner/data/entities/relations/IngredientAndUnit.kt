package de.writer_chris.babittmealplaner.data.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import de.writer_chris.babittmealplaner.data.entities.Ingredient
import de.writer_chris.babittmealplaner.data.entities.UnitType

data class IngredientAndUnit(
    @Embedded val ingredient: Ingredient,
    @Relation(
        parentColumn = "unitType",
        entityColumn = "unitType"
    )
    val unitType: UnitType
)