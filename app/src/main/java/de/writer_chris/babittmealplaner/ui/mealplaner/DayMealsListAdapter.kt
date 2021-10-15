package de.writer_chris.babittmealplaner.ui.mealplaner

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.data.Repository

import de.writer_chris.babittmealplaner.databinding.ItemMealDayBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.model.DayMeals


class DayMealsListAdapter(private val onItemClick: (DayMeals) -> Unit) :
    ListAdapter<DayMeals, DayMealsListAdapter.MealViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DayMeals>() {
            override fun areItemsTheSame(oldItem: DayMeals, newItem: DayMeals): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: DayMeals, newItem: DayMeals): Boolean {
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
        fun bind(dayMeals: DayMeals) {
            val sdf = SimpleDateFormat("EEE - dd.MM.yyyy")
            val cal = Calendar.getInstance()
            cal.timeInMillis = dayMeals.date

            binding.apply {
                txtDate.text = sdf.format(cal).toString()
                txtBreakfastDish.text = if(dayMeals.breakfast.dishId!=null){"*"}else{dayMeals.breakfast.dishId.toString()}
                txtLunchDish.text = dayMeals.lunch.mealType
                txtDinnerDish.text = dayMeals.dinner.mealType
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