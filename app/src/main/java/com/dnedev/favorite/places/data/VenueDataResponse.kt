package com.dnedev.favorite.places.data

import com.dnedev.favorite.places.ui.venues.VenueItemUiModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VenueDataResponse(
    val id: String,
    val name: String,
    @Json(name = "location")
    val locationData: VenueLocation
)

@JsonClass(generateAdapter = true)
data class VenueLocation(
    @Json(name = "lat")
    val latitude: Double,
    @Json(name = "lng")
    val longitude: Double
)

fun VenueDataResponse.covertToVenueItemUiModel() = VenueItemUiModel(
    id = this.id,
    name = this.name,
    longitude = this.locationData.longitude,
    latitude = this.locationData.latitude
)