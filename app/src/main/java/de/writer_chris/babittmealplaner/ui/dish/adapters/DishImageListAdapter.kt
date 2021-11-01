package de.writer_chris.babittmealplaner.ui.dish.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.load
import coil.request.ImageRequest
import de.writer_chris.babittmealplaner.R
import de.writer_chris.babittmealplaner.data.utility.DataUtil
import de.writer_chris.babittmealplaner.data.utility.TEMPORAL_FILE_NAME
import de.writer_chris.babittmealplaner.data.utility.TITLE
import de.writer_chris.babittmealplaner.databinding.ItemDishImageBinding
import de.writer_chris.babittmealplaner.network.DishPhoto

class DishImageListAdapter(private val navigateToDishEditFragment: () -> Unit) :
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

    class DishPhotoViewHolder(
        private var binding: ItemDishImageBinding,
        private val context: Context,
        val navigateToDishEditFragment: () -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(dishPhoto: DishPhoto) {
            binding.apply {
                val imgUri = dishPhoto.previewURL.toUri().buildUpon().scheme("https").build()
                val request = ImageRequest.Builder(context).data(imgUri).target(
                    onStart = { placeholder -> imgViewDishImage.setImageResource(R.drawable.loading_animation) },
                    onError = { error -> imgViewDishImage.setImageResource(R.drawable.ic_broken_image_96) },
                    onSuccess = { result ->
                        imgViewDishImage.load(result)
                        imgViewDishImage.setOnClickListener {
                            DataUtil.saveDishPictureToInternalStorage(
                                context,
                                TEMPORAL_FILE_NAME,
                                result.toBitmap()
                            )
                            navigateToDishEditFragment()
                        }
                    }
                ).build()

                Coil.enqueue(request)
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
            ), parent.context, navigateToDishEditFragment
        )
    }

    override fun onBindViewHolder(holder: DishPhotoViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }


}