package de.writer_chris.babittmealplaner.ui.mealplaner.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.data.entities.relations.MealAndDish
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.databinding.ItemMealDayBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.models.DayMealsAndDish


class DayMealsListAdapter(
    private val context: Context,
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
            context: Context,
            onItemSelect: (mealId: Int) -> Unit,
            onItemRead: (dishMealId: Array<Int>) -> Unit
        ) {
            binding.apply {
                txtDate.text = getDate(dayMeals.date)
                txtDayOfTheWeek.text = getDayOfWeek(dayMeals.date)
                recyclerViewDayMeals.adapter =
                    getAdapter(dayMeals, context, onItemSelect, onItemRead)
            }
        }

        private fun getDate(date: Long): String {
            return CalendarUtil.longToGermanDate(date)
        }

        private fun getDayOfWeek(date: Long): String {
            return CalendarUtil.longToWeekday(date)
        }

        private fun getAdapter(
            dayMeals: DayMealsAndDish,
            context: Context,
            onItemSelect: (mealId: Int) -> Unit,
            onItemRead: (dishMealId: Array<Int>) -> Unit
        ): DayMealItemListAdapter {
            val itemList: List<MealAndDish> =
                listOf(dayMeals.breakfast, dayMeals.lunch, dayMeals.dinner)
            val adapter = DayMealItemListAdapter(context, onItemSelect, onItemRead)
            adapter.submitList(itemList)
            return adapter
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
        holder.bind(current, context, onItemSelect, onItemRead)
    }
}