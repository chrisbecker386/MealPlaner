package de.writer_chris.babittmealplaner.data.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.entities.DishIngredientCrossRef
import de.writer_chris.babittmealplaner.data.entities.Ingredient

data class DishWithIngredients(
    @Embedded val dish: Dish,
    @Relation(
        parentColumn = "dishId",
        entityColumn = "ingredientId",
        associateBy = Junction(DishIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)
