package de.writer_chris.babittmealplaner.ui.mealplaner.models


import de.writer_chris.babittmealplaner.data.entities.Meal

data class DayMeals(val date: Long, val breakfast: Meal, val lunch: Meal, val dinner: Meal)