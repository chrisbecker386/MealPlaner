package de.writer_chris.babittmealplaner.network

data class PixaBayPhotos(
    val hits: List<DishPhoto>,
    val total: Int,
    val totalHits: Int
)