package de.writer_chris.babittmealplaner.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId:Int=0,
    val name:String,
    val unitType:String
    )
