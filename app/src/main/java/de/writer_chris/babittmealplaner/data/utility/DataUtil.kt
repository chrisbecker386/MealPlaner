package de.writer_chris.babittmealplaner.data.utility

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.IOException
import java.lang.Exception

class DataUtil {
    companion object {

        fun deletePhotoFromInternalStorage(context: Context, filename: String): Boolean {
            return try {
                context.deleteFile("$filename.jpg")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun loadDishPictureFromInternalStorage(context: Context, filename: String): Bitmap? {
            return try {
                val files = context.filesDir.listFiles()
                    .filter { it.canRead() && it.isFile && it.name == "$filename.jpg" }
                    .map {
                        val bytes = it.readBytes()
                        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    }
                files[0]
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun isFileExists(context: Context, filename: String): Boolean {
            return try {
                val file: File = File(context.filesDir, "$filename.jpg")
                file.exists()

            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
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