package de.writer_chris.babittmealplaner.ui.dish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.databinding.ItemDishBinding

class DishListAdapter(private val onItemClick: (Dish) -> Unit) :
    ListAdapter<Dish, DishListAdapter.DishViewHolder>(DiffCallback) {

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
                dishName.text = dish.dishName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        return DishViewHolder(ItemDishBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onItemClick(current) }
        holder.bind(current)
    }


}