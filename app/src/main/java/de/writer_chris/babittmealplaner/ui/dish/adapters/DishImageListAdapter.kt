package de.writer_chris.babittmealplaner.ui.dish.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.databinding.ItemDishImageBinding
import de.writer_chris.babittmealplaner.network.DishPhoto

class DishImageListAdapter :
    ListAdapter<DishPhoto, DishImageListAdapter.DishPhotoViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DishPhoto>() {
            override fun areItemsTheSame(
                oldItem: DishPhoto,
                newItem: DishPhoto
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DishPhoto,
                newItem: DishPhoto
            ): Boolean {
                return (oldItem.previewURL == newItem.previewURL)
            }
        }
    }

    class DishPhotoViewHolder(private var binding: ItemDishImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dishPhoto: DishPhoto) {
            binding.apply {
//                imgViewDishImage.setImageURI(
//                    dishPhoto.previewURL.toUri().buildUpon().scheme("https").build()
//                )
                val imgUri = dishPhoto.previewURL.toUri().buildUpon().scheme("https").build()
                imgViewDishImage.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_broken_image_96)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DishPhotoViewHolder {
        return DishPhotoViewHolder(
            ItemDishImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DishPhotoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }
}