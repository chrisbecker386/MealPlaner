package de.writer_chris.babittmealplaner.data.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.security.Permission

class Permissions {

    companion object {

        fun hasWritePermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        }

        fun hasReadPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun permissionRequestWriteExternal(context: Context) {
            val permissionsToRequest = mutableListOf<String>()
            if (!hasWritePermission(context)) {
                permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (permissionsToRequest.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissionsToRequest.toTypedArray(),
                    PermissionCode.WRITE_EXTERNAL.requestCode
                )
            }
        }

        fun permissionRequestReadExternal(context: Context) {
            val permissionsToRequest = mutableListOf<String>()
            if (!hasReadPermission(context)) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (permissionsToRequest.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    permissionsToRequest.toTypedArray(),
                    PermissionCode.READ_EXTERNAL.requestCode
                )
            }
        }

    }
}