package de.writer_chris.babittmealplaner.ui.mealplaner.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.entities.relations.MealAndDish
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.data.utility.MealTypes
import de.writer_chris.babittmealplaner.databinding.ItemMealDishBinding
class DayMealItemListAdapter(
    private val context: Context,
    private val onItemSelect: (mealId: Int) -> Unit,
    private val onItemRead: (dishMealId: Array<Int>) -> Unit
) :
    ListAdapter<MealAndDish, DayMealItemListAdapter.MealAndDishViewHolder>(DiffCallback) {
    companion object {

        private val DiffCallback = object : DiffUtil.ItemCallback<MealAndDish>() {
            override fun areItemsTheSame(oldItem: MealAndDish, newItem: MealAndDish): Boolean {
                return oldItem.meal.mealId == newItem.meal.mealId
            }

            override fun areContentsTheSame(oldItem: MealAndDish, newItem: MealAndDish): Boolean {
                return oldItem.meal.mealType == newItem.meal.mealType &&
                        oldItem.meal.dishId == newItem.meal.dishId
            }
        }
    }

    class MealAndDishViewHolder(private var binding: ItemMealDishBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            mealAndDish: MealAndDish,
            context: Context,
            onItemSelect: (mealId: Int) -> Unit,
            onItemRead: (dishMealId: Array<Int>) -> Unit
        ) {
            setDefaults(mealAndDish)
            setImageAndTitle(mealAndDish, context, onItemSelect, onItemRead)
        }

        private fun setDefaults(mealAndDish: MealAndDish) {
            binding.apply {
                txtMealType.text = mealAndDish.meal.mealType
                when (mealAndDish.meal.mealType) {
                    MealTypes.BREAKFAST.title -> imgBtnDish.setImageResource(R.drawable.ic_breakfast)
                    MealTypes.LUNCH.title -> imgBtnDish.setImageResource(R.drawable.ic_lunch)
                    MealTypes.DINNER.title -> imgBtnDish.setImageResource(R.drawable.ic_dinner)
                }
            }
        }

        private fun setImageAndTitle(
            mealAndDish: MealAndDish,
            context: Context,
            onItemSelect: (mealId: Int) -> Unit,
            onItemRead: (dishMealId: Array<Int>) -> Unit
        ) {
            binding.apply {
                if (mealAndDish.meal.dishId == null) {
                    txtDishName.text = "-"
                    imgBtnDish.setOnClickListener {
                        onItemSelect(mealAndDish.meal.mealId)
                    }
                } else {
                    txtDishName.text = mealAndDish.dish?.dishName
                    imgBtnDish.setImageBitmap(
                        DataUtil.loadDishPictureFromInternalStorage(
                            context,
                            mealAndDish.dish?.dishId.toString()
                        )
                    )
                    imgBtnDish.setOnClickListener {
                        onItemRead(
                            arrayOf(
                                mealAndDish.dish?.dishId ?: -1,
                                mealAndDish.meal.mealId
                            )
                        )
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealAndDishViewHolder {
        return MealAndDishViewHolder(
            ItemMealDishBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MealAndDishViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, context, onItemSelect, onItemRead)
    }
}