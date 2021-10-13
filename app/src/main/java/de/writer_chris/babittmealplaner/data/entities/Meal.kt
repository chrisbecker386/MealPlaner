package de.writer_chris.babittmealplaner.data.entities

import androidx.annotation.Nullable

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Meal(
    @PrimaryKey(autoGenerate = true) val mealId: Int = 0,
    val dishId:Int?,
    val date:Long,
    val mealType: String,
    val periodId: Int

)
{
}