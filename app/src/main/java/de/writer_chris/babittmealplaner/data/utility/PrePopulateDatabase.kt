package de.writer_chris.babittmealplaner.data.utility

import android.content.Context
import android.icu.util.Calendar
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.Period
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil.Companion.getCalendarDate
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil.Companion.getNormalizedCalender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class PrePopulateDatabase() {
    companion object {
        fun prePopulateDatabase(context: Context) {
            val repo = Repository(context)
            insertDish(repo)
            insertPeriod(repo)
            insertMeals(repo)
        }

        private fun insertDish(repo: Repository) {
            CoroutineScope(IO).launch {
                DISH_LIST_STARTUP.forEach {
                    repo.insertDish(it)
                }
            }
            //TODO and copy pictures for the dishes
        }


        private fun insertPeriod(repo: Repository) {
            val cal = Calendar.getInstance()
            val start = getNormalizedCalender(cal).timeInMillis
            val end = getCalendarDate(cal.timeInMillis, 6).timeInMillis

            CoroutineScope(IO).launch {
                repo.insertPeriod(
                    Period(1, start, end)
                )
            }
        }

        private fun insertMeals(repo: Repository) {
            val dayDish = listOf("breakfast", "lunch", "dinner")
            val day = getNormalizedCalender(Calendar.getInstance())
            CoroutineScope(IO).launch {
                repeat(7) {
                    dayDish.forEach {
                        repo.insertMeal(
                            Meal(
                                dishId = null,
                                date = day.timeInMillis,
                                mealType = it,
                                periodId = 1
                            )
                        )

                    }
                    day.add(Calendar.DATE, 1)
                }
            }
        }
    }


}