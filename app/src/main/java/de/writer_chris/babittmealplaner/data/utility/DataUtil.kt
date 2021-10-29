package de.writer_chris.babittmealplaner.data.utility

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.lang.Exception

class DataUtil {
    companion object {

        private fun deletePhotoFromInternalStorage(context: Context, dishId: Int): Boolean {
            return try {
                context.deleteFile("$dishId.jpg")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        private fun loadDishPictureFromInternalStorage(context: Context, id: Int): Bitmap? {
            val files = context.filesDir.listFiles()
                .filter { it.canRead() && it.isFile && it.name == "$id.jpg" }
                .map {
                    val bytes = it.readBytes()
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                }

            return files[0] ?: null
        }

        fun saveDishPictureToInternalStorage(
            context: Context,
            filename: String,
            bmp: Bitmap
        ): Boolean {
            return try {
                context.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                    if (!bmp.compress(
                            Bitmap.CompressFormat.JPEG, 95, stream
                        )
                    ) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

    }
}