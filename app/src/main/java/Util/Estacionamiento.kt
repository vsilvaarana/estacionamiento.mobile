package Util

data class Estacionamiento(
    val id: String,
    val floor: Int,
    val isOccupied: Boolean,
    val isSelected: Boolean = false
)