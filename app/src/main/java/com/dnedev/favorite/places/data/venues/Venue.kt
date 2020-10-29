package com.dnedev.favorite.places.data.venues

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dnedev.favorite.places.ui.venues.VenueItemUiModel

//TODO add is_favorite column

@Entity(tableName = "venues")
data class Venue(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "category_id")
    val categoryId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

fun Venue.convertToVenueItemUiModel() = VenueItemUiModel(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    categoryId = categoryId
).apply {
    isAddedAsFavorite = true
}