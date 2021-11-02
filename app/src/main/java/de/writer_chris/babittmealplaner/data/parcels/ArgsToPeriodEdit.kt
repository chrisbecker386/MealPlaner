package de.writer_chris.babittmealplaner.data.parcels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArgsToPeriodEdit(val title: String, val periodId: Int) : Parcelable
