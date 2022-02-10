package de.writer_chris.babittmealplaner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UnitType(
    @PrimaryKey(autoGenerate = false)
    val unitType: String
)
