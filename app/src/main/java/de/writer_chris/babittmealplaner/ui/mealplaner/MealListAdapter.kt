package de.writer_chris.babittmealplaner.ui.mealplaner

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.data.utility.CalendarUtil
import de.writer_chris.babittmealplaner.databinding.ItemMealDayBinding

class MealListAdapter :
    ListAdapter<List<MealWithDish>, MealListAdapter.MealViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<List<MealWithDish>>() {
            override fun areItemsTheSame(
                oldItem: List<MealWithDish>,
                newItem: List<MealWithDish>
            ): Boolean {
                return oldItem[0].date == newItem[0].date
            }

            override fun areContentsTheSame(
                oldItem: List<MealWithDish>,
                newItem: List<MealWithDish>
            ): Boolean {
                return oldItem[0].date == newItem[0].date
            }
        }
    }

    class MealViewHolder(private var binding: ItemMealDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mealWithDishList: List<MealWithDish>) {
            binding.apply {
                txtDayOfTheWeek.setText(CalendarUtil.longToWeekdayResId(mealWithDishList[0].date))
                txtDate.text = CalendarUtil.longToGermanDate(mealWithDishList[0].date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(ItemMealDayBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val current = getItem(position)
        val toast = Toast.makeText(holder.itemView.context, "dada", LENGTH_SHORT)
        holder.itemView.setOnClickListener { toast.show() }
        holder.bind(current)
    }


}