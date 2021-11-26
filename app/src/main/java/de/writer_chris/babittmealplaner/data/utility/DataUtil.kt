package de.writer_chris.babittmealplaner.data.utility

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import de.writer_chris.babittmealplaner.data.utility.FileName.*
import java.io.File
import java.io.IOException
import java.lang.Exception

class DataUtil {
    companion object {

        fun deletePhotoFromInternalStorage(context: Context, filename: String): Boolean {
            return try {
                context.deleteFile("$filename.${JPG.fileString}")
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        fun loadDishPictureFromInternalStorage(context: Context, filename: String): Bitmap? {
            return try {
                val files = context.filesDir.listFiles()
                    .filter { it.canRead() && it.isFile && it.name == "$filename.${JPG.fileString}" }
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
                val file = File(context.filesDir, "$filename.${JPG.fileString}")
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
                context.openFileOutput("$filename.${JPG.fileString}", MODE_PRIVATE).use { stream ->
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

        fun savePdfToInternalStorage(context: Context, pdf: PdfDocument): Boolean {

            return try {
                context.openFileOutput(INTERNAL_PDF_NAME.fileString, MODE_PRIVATE).use {
                    pdf.writeTo(it)
                }
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        // Checks if a volume containing external storage is available
        // for read and write.
        private fun isExternalStorageWritable(): Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }

        fun writePdfToDownloads(
            context: Context,
            pdf: PdfDocument,
            filename: String
        ): Boolean {
            handlePermissionWrite(context)

            if (!isExternalStorageWritable()) {
                Log.d("Save", "External storage is not writable")
                return false
            }

            val path = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, filename)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            }
            return try {
                context.contentResolver.insert(path, contentValues)?.also { uri ->
                    context.contentResolver.openOutputStream(uri).use { out ->
                        pdf.writeTo(out)
                    }
                } ?: throw IOException("Couldn't save file")
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

        private fun handlePermissionWrite(context: Context) {
            if (!Permissions.hasWritePermission(context)) {
                Permissions.permissionRequestWriteExternal(context)
            }
        }

        private fun handlePermissionRead(context: Context) {
            if (!Permissions.hasReadPermission(context)) {
                Permissions.permissionRequestReadExternal(context)
            }
        }
    }
}


