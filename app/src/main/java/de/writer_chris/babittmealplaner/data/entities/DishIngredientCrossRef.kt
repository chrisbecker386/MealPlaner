package de.writer_chris.babittmealplaner.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["dishId", "ingredientId"])
data class DishIngredientCrossRef(
    val dishId: Int,
    val ingredientId: Int
)