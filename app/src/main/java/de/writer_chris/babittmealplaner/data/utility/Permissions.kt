package de.writer_chris.babittmealplaner.data.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permissions {
    companion object {

        fun hasWritePermission(context: Context) = ContextCompat.checkSelfPermission(
            context, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

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
    }
}