package de.writer_chris.babittmealplaner.data.parcels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArgsToDishDetails(val title: String, val dishId: Int, val mealId: Int?) : Parcelable
