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
import androidx.core.net.toUri
import de.writer_chris.babittmealplaner.data.utility.FileName.INTERNAL_PDF_NAME
import de.writer_chris.babittmealplaner.data.utility.FileName.JPG
import java.io.File
import java.io.IOException

class DataUtil {
    companion object {

        fun deletePhotoFromInternalStorage(context: Context, filename: String) = try {
            context.deleteFile("$filename.${JPG.fileString}")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

        fun loadDishPictureFromInternalStorage(context: Context, filename: String) = try {
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

        fun isFileExists(context: Context, filename: String) = try {
            val file = File(context.filesDir, "$filename.${JPG.fileString}")
            file.exists()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

        fun saveDishPictureToInternalStorage(
            context: Context,
            filename: String,
            bmp: Bitmap
        ) = try {
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

        fun savePdfToInternalStorage(context: Context, pdf: PdfDocument) = try {
            context.openFileOutput(INTERNAL_PDF_NAME.fileString, MODE_PRIVATE).use {
                pdf.writeTo(it)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }

        private fun isExternalStorageWritable() =
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

        fun writePdfToDownloads(
            context: Context,
            pdf: PdfDocument,
            filename: String
        ): Boolean {
            handlePermissionWrite(context)

            if (!isExternalStorageWritable()) {
                return false
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
            } else {
                val path =
                    Environment.getExternalStorageDirectory().path + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + filename
                val uri = File(path).toUri()
                return try {
                    context.contentResolver.openOutputStream(uri)
                        .use { pdf.writeTo(it) }
                    true
                } catch (e: IOException) {
                    e.printStackTrace()
                    false
                }
            }
        }

        private fun handlePermissionWrite(context: Context) {
            if (!Permissions.hasWritePermission(context)) {
                Permissions.permissionRequestWriteExternal(context)
            }
        }
    }
}