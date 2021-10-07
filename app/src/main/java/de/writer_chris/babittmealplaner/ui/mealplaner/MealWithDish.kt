package de.writer_chris.babittmealplaner.ui.mealplaner

import java.time.LocalDate

data class MealWithDish(val date: Long, val mealType: String, val dishName: String = mealType)
