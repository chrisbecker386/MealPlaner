package de.writer_chris.babittmealplaner.ui.mealplaner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.databinding.ItemMealDayPdfBinding
import de.writer_chris.babittmealplaner.ui.mealplaner.models.DayMealsAndDish

class DayMealsPdfListAdapter :
    ListAdapter<DayMealsAndDish, DayMealsPdfListAdapter.DayMealsAndDishViewHolder>(
        DiffCallback
    ) {
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
                return oldItem.date == newItem.date
            }

        }
    }


    class DayMealsAndDishViewHolder(private var binding: ItemMealDayPdfBinding) :
        RecyclerView.ViewHolder(binding.root) {
//    ListView.FixedViewInfo()
    fun bind(dayMealsAndDish: DayMealsAndDish) {
            binding.apply {
                date.text = dayMealsAndDish.date.toString()
                breakfast.text = dayMealsAndDish.breakfast.dish?.dishName ?: "-"
                lunch.text = dayMealsAndDish.lunch.dish?.dishName ?: "-"
                dinner.text = dayMealsAndDish.dinner.dish?.dishName ?: "-"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayMealsAndDishViewHolder {
        return DayMealsAndDishViewHolder(
            ItemMealDayPdfBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DayMealsAndDishViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }


}