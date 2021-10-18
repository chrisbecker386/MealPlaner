package de.writer_chris.babittmealplaner.ui.mealplaner.model

import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.relations.MealAndDish

class DayMealsAndDish(
    val date: Long,
    val breakfast: MealAndDish,
    val lunch: MealAndDish,
    val dinner: MealAndDish
) {
}