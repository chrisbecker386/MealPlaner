package de.writer_chris.babittmealplaner.data.parcels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArgsToDish(val mealId: Int) : Parcelable
