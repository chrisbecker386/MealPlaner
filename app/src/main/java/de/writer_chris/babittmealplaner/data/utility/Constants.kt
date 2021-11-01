package de.writer_chris.babittmealplaner.data.utility


import de.writer_chris.babittmealplaner.R

enum class TITLE(resId: Int) {
    EDIT_DISH(R.string.edit_dish),
    MEAL_PLANER(R.string.title_meal_planer),
    SCHEDULE(R.string.title_actual_meal_plan),
    DISH(R.string.title_dish),
    ADD_DISH(R.string.title_add_dish),
    RECIPE(R.string.title_dish_detail),
    IMAGE_SELECTION(R.string.title_dish_image_selection)
}


const val TEMPORAL_FILE_NAME = "temp"