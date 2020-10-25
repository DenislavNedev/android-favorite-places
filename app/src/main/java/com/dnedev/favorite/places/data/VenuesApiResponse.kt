package com.dnedev.favorite.places.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VenuesApiResponse(
    @Json(name = "meta")
    val metaDataResponse: MetaDataResponse,
    @Json(name = "response")
    val venuesResponse: VenuesDataResponse
)

@JsonClass(generateAdapter = true)
data class VenuesDataResponse(
    val venues: List<VenueDataResponse>?
)