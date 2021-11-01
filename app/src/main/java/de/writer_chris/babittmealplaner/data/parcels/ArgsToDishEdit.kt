package de.writer_chris.babittmealplaner.data.parcels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArgsToDishEdit(
    val title: String,
    val dishId: Int,
    val name: String?,
    val duration: String?,
    val description: String?
) : Parcelable
