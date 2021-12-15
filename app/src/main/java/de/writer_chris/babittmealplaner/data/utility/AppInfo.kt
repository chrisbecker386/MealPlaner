package de.writer_chris.babittmealplaner.data.utility


import android.content.Context
import de.writer_chris.babittmealplaner.BuildConfig
import de.writer_chris.babittmealplaner.R

data class Information(val info: String, val value: String)

class AppInfo {
    companion object {
        fun getAppInfoList(context: Context): List<Information> {
            return listOf(
                Information(
                    context.getString(R.string.info_app),
                    context.getString(R.string.app_name)
                ),
                Information(
                    context.getString(R.string.info_version),
                    BuildConfig.VERSION_NAME
                ),
                Information(
                    context.getString(R.string.info_author),
                    context.getString(R.string.app_author)
                )
            )
        }
    }
}