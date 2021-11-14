package de.writer_chris.babittmealplaner.ui.mealplaner.models

import de.writer_chris.babittmealplaner.data.entities.relations.MealAndDish

class DayMealsAndDish(
    var date: Long,
    val breakfast: MealAndDish,
    val lunch: MealAndDish,
    val dinner: MealAndDish
)