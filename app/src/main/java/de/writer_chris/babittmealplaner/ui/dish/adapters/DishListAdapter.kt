package de.writer_chris.babittmealplaner.ui.dish.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.entities.Dish
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.databinding.ItemDishBinding

class DishListAdapter(
    private val context: Context,
    private val onItemClick: (Dish) -> Unit,
    private val onItemLongClick: (Dish) -> Unit
) :
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
        fun bind(context: Context, dish: Dish) {
            binding.apply {
                txtDishNameDishItem.text = dish.dishName

                if (DataUtil.isFileExists(context, dish.dishId.toString())) {
                    imgDishItem.setImageBitmap(
                        DataUtil.loadDishPictureFromInternalStorage(
                            context,
                            dish.dishId.toString()
                        )
                    )
                } else {
                    imgDishItem.setImageResource(R.drawable.ic_broken_image_96)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        return DishViewHolder(
            ItemDishBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val current = getItem(position)
        holder.apply {
            itemView.setOnClickListener { onItemClick(current) }
            itemView.setOnLongClickListener {
                onItemLongClick(current)
                return@setOnLongClickListener true
            }
            bind(context, current)
        }
    }
}