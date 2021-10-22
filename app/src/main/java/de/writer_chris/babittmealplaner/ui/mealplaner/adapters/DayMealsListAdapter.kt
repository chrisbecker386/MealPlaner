package de.writer_chris.babittmealplaner.ui.mealplaner.adapters

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.R

import de.writer_chris.babittmealplaner.databinding.ItemMealDayBinding

import de.writer_chris.babittmealplaner.ui.mealplaner.models.DayMealsAndDish


class DayMealsListAdapter(private val onItemClick: (mealId: Int) -> Unit) :
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
        fun bind(dayMeals: DayMealsAndDish, onItemClick: (mealId: Int) -> Unit) {

            //TODO make it more nice!!
            val sdf = SimpleDateFormat("EEE - dd.MM.yyyy")
            val cal = Calendar.getInstance()
            cal.timeInMillis = dayMeals.date


            binding.apply {
                txtDate.text = sdf.format(cal).toString()
                imgbtnBreakfast.setOnClickListener {
                    onItemClick(dayMeals.breakfast.meal.mealId)
                }
                imgbtnLunch.setOnClickListener {
                    onItemClick(dayMeals.lunch.meal.mealId)
                }
                imgbtnDinner.setOnClickListener {
                    onItemClick(dayMeals.dinner.meal.mealId)
                }

                if (dayMeals.breakfast.meal.dishId == null) {
                    txtBreakfastDish.text = "-"
                    imgbtnBreakfast.setImageResource(R.drawable.ic_breakfast)

                } else {
                    txtBreakfastDish.text = dayMeals.breakfast.dish?.dishName
                    //TODO change when Dish has resource
                    imgbtnBreakfast.setImageResource(R.drawable.ic_breakfast)
                }
                if (dayMeals.lunch.meal.dishId == null) {
                    txtLunchDish.text = "-"
                    imgbtnLunch.setImageResource(R.drawable.ic_lunch)

                } else {
                    txtLunchDish.text = dayMeals.lunch.dish?.dishName
                    //TODO change when Dish has resource
                    imgbtnLunch.setImageResource(R.drawable.ic_lunch)
                }

                if (dayMeals.dinner.meal.dishId == null) {
                    txtDinnerDish.text = "-"
                    imgbtnDinner.setImageResource(R.drawable.ic_dinner)

                } else {
                    txtDinnerDish.text = dayMeals.dinner.dish?.dishName
                    //TODO change when Dish has resource
                    imgbtnDinner.setImageResource(R.drawable.ic_dinner)
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
        holder.bind(current, onItemClick)

    }


}