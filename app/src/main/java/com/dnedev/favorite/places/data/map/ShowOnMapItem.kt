package com.dnedev.favorite.places.data.map

import com.dnedev.favorite.places.ui.venues.VenueItemUiModel
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowOnMapItem(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val categoryId: String,
    val isAddedAsFavorite: Boolean
)

fun ShowOnMapItem.convertToVenueItemUiModel() = VenueItemUiModel(
    id = id,
    name = name,
    latitude = latitude,
    longitude = longitude,
    categoryId = categoryId
).apply {
    isAddedAsFavorite = isAddedAsFavorite
}