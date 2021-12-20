package de.writer_chris.babittmealplaner.data.utility

import android.content.Context
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.Repository
import de.writer_chris.babittmealplaner.data.entities.Meal
import de.writer_chris.babittmealplaner.data.entities.Period
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil.Companion.getCalendarDate
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil.Companion.getNormalizedCalender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class PrePopulateApp() {
    companion object {
        fun prePopulateApp(context: Context) {
            val repo = Repository(context)
            insertDish(repo)
            insertDishPictures(context)
            insertPeriod(repo)
            insertMeals(repo)

        }

        private fun insertDish(repo: Repository) {
            CoroutineScope(IO).launch {
                DISH_LIST_STARTUP.forEach {
                    repo.insertDish(it)
                }
            }
        }

        private fun insertDishPictures(context: Context) {
            DataUtil.saveDishPictureToInternalStorage(
                context,
                "1",
                BitmapFactory.decodeResource(context.resources, R.drawable.pic1)
            )
            DataUtil.saveDishPictureToInternalStorage(
                context,
                "2",
                BitmapFactory.decodeResource(context.resources, R.drawable.pic2)
            )

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