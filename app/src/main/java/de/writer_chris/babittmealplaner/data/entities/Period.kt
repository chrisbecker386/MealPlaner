package de.writer_chris.babittmealplaner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Period(@PrimaryKey val id: Int, val startDate: Long, val endDate: Long) {
}