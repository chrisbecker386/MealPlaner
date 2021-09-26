package de.writer_chris.babittmealplaner

import android.app.Application
import de.writer_chris.babittmealplaner.data.AppRoomDatabase

class BabittMealPlanerApplication : Application() {
    val database: AppRoomDatabase by lazy {
        AppRoomDatabase.getDatabase(this)
    }
}