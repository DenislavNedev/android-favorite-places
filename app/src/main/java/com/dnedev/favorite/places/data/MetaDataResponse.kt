package com.dnedev.favorite.places.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetaDataResponse(
    val code: Int,
    val requestId: String
)