package de.writer_chris.babittmealplaner.data.utility

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
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

        fun savePdfToInternalStorage(context:Context, pdf:PdfDocument):Boolean{
            return try {
                context.openFileOutput("HelloPDfWorld.pdf", MODE_PRIVATE).use {
                    pdf.writeTo(it)
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        fun isExternalStorageWritable(context: Context): Boolean {
            return Permissions.hasWritePermission(context)
        }

        fun writeFileToExternalStorage(context: Context, filename: String): Boolean {
            if (!isExternalStorageWritable(context)) {
                return false
            }
            val file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "/$filename.pdf"
            )


            return true
        }

        fun savePdfToExternalStorage(context: Context, filename: String, pdf: PdfDocument) {
            val pdfCollection = sdk29AndUp {
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: { MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL) }

            val contentValues = ContentValues().apply {
                put(MediaStore.Files.FileColumns.DISPLAY_NAME, "$filename.pdf")
                put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        }


    }


}


inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else {
        null
    }
}