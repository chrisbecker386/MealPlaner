package de.writer_chris.babittmealplaner.ui.mealplaner.adapters

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import de.writer_chris.babittmealplaner.databinding.ItemMealDayBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.models.DayMealsAndDish


class DayMealsListAdapter(private val onItemClick: (DayMealsAndDish) -> Unit) :
//    ListAdapter<DayMeals, DayMealsListAdapter.MealViewHolder>(DiffCallback) {

    ListAdapter<DayMealsAndDish, DayMealsListAdapter.MealViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DayMealsAndDish>() {
            override fun areItemsTheSame(oldItem: DayMealsAndDish, newItem: DayMealsAndDish): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: DayMealsAndDish, newItem: DayMealsAndDish): Boolean {
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
        fun bind(dayMeals: DayMealsAndDish) {
            val sdf = SimpleDateFormat("EEE - dd.MM.yyyy")
            val cal = Calendar.getInstance()
            cal.timeInMillis = dayMeals.date

            binding.apply {
                txtDate.text = sdf.format(cal).toString()
                txtBreakfastDish.text = if(dayMeals.breakfast.meal.dishId!=null){dayMeals.breakfast.dish?.dishName}else{dayMeals.breakfast.meal.mealType.toString()}
                txtLunchDish.text = dayMeals.lunch.meal.mealType
                txtDinnerDish.text = dayMeals.dinner.meal.mealType
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
        holder.itemView.setOnClickListener { onItemClick(current) }
        holder.bind(current)
    }


}