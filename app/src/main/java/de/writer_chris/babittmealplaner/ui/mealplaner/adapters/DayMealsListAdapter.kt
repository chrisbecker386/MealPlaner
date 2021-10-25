package de.writer_chris.babittmealplaner.ui.mealplaner.adapters

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.R

import de.writer_chris.babittmealplaner.databinding.ItemMealDayBinding

import de.writer_chris.babittmealplaner.ui.mealplaner.models.DayMealsAndDish


class DayMealsListAdapter(
    private val onItemSelect: (mealId: Int) -> Unit,
    private val onItemRead: (dishMealId: Array<Int>) -> Unit
) :
    ListAdapter<DayMealsAndDish, DayMealsListAdapter.MealViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DayMealsAndDish>() {
            override fun areItemsTheSame(
                oldItem: DayMealsAndDish,
                newItem: DayMealsAndDish
            ): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(
                oldItem: DayMealsAndDish,
                newItem: DayMealsAndDish
            ): Boolean {
                return (oldItem.date == newItem.date
                        && oldItem.breakfast == newItem.breakfast
                        && oldItem.lunch == newItem.lunch
                        && oldItem.dinner == newItem.dinner
                        )
            }
        }
    }

    class MealViewHolder(private var binding: ItemMealDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            dayMeals: DayMealsAndDish,
            onItemSelect: (mealId: Int) -> Unit,
            onItemRead: (dishMealId: Array<Int>) -> Unit
        ) {

            //TODO make it more nice!!
            val sdf = SimpleDateFormat("EEE - dd.MM.yyyy")
            val cal = Calendar.getInstance()
            cal.timeInMillis = dayMeals.date
            binding.apply {
                txtDate.text = sdf.format(cal).toString()

                if (dayMeals.breakfast.meal.dishId == null) {
                    txtBreakfastDish.text = "-"
                    imgbtnBreakfast.setImageResource(R.drawable.ic_breakfast)
                    imgbtnBreakfast.setOnClickListener {
                        onItemSelect(dayMeals.breakfast.meal.mealId)
                    }

                } else {
                    txtBreakfastDish.text = dayMeals.breakfast.dish?.dishName
                    //TODO change when Dish image has resource
                    imgbtnBreakfast.setImageResource(R.drawable.ic_breakfast)
                    imgbtnBreakfast.setOnClickListener {
                        onItemRead(
                            arrayOf(
                                dayMeals.breakfast.dish?.dishId ?: -1,
                                dayMeals.breakfast.meal.mealId
                            )
                        )
                    }
                }
                if (dayMeals.lunch.meal.dishId == null) {
                    txtLunchDish.text = "-"
                    imgbtnLunch.setOnClickListener {
                        onItemSelect(dayMeals.lunch.meal.mealId)
                    }
                    imgbtnLunch.setImageResource(R.drawable.ic_lunch)

                } else {
                    txtLunchDish.text = dayMeals.lunch.dish?.dishName
                    //TODO change when Dish image has resource
                    imgbtnLunch.setImageResource(R.drawable.ic_lunch)
                    imgbtnLunch.setOnClickListener {
                        onItemRead(
                            arrayOf(
                                dayMeals.lunch.dish?.dishId ?: -1,
                                dayMeals.lunch.meal.mealId
                            )
                        )
                    }
                }

                if (dayMeals.dinner.meal.dishId == null) {
                    txtDinnerDish.text = "-"
                    imgbtnDinner.setImageResource(R.drawable.ic_dinner)
                    imgbtnDinner.setOnClickListener {
                        onItemSelect(dayMeals.dinner.meal.mealId)
                    }

                } else {
                    txtDinnerDish.text = dayMeals.dinner.dish?.dishName
                    //TODO change when Dish image has resource
                    imgbtnDinner.setImageResource(R.drawable.ic_dinner)
                    imgbtnDinner.setOnClickListener {
                        onItemRead(
                            arrayOf(
                                dayMeals.dinner.dish?.dishId ?: -1,
                                dayMeals.dinner.meal.mealId
                            )
                        )
                    }
                }


            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealViewHolder {
        return MealViewHolder(
            ItemMealDayBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )


        )
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onItemSelect, onItemRead)

    }


}