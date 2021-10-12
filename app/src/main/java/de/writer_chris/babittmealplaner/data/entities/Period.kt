package de.writer_chris.babittmealplaner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Period(
    @PrimaryKey(autoGenerate = true) val periodId: Int = 0,
    val startDate: Long,
    val endDate: Long
) {
}