package de.writer_chris.babittmealplaner.data.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity
data class Dish(
    @PrimaryKey(autoGenerate = true) val dishId: Int = 0,
    @ColumnInfo(name = "dish_name") @NotNull val dishName: String,
    val duration: Long = 0,
    val description: String = ""
)

//TODO create an TimeFormatter extension