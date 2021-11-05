package de.writer_chris.babittmealplaner.ui.mealplaner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.parcels.ArgsToDish
import de.writer_chris.babittmealplaner.databinding.ItemDishBinding

class MealDishListAdapter(
    private val onItemClick: (Dish) -> Unit
) :
    ListAdapter<Dish, MealDishListAdapter.DishViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Dish>() {
            override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem.dishId == newItem.dishId
            }

            override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem.dishName == newItem.dishName
            }
        }
    }

    class DishViewHolder(private var binding: ItemDishBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dish: Dish) {
            binding.apply {
                txtInputEditDishName.text = dish.dishName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        return DishViewHolder(ItemDishBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val current = getItem(position)
        holder.apply {
            itemView.setOnClickListener { onItemClick(current) }
            bind(current)
        }
    }
}