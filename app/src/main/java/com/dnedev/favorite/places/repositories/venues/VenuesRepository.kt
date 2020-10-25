package com.dnedev.favorite.places.repositories.venues

import com.dnedev.favorite.places.BuildConfig
import com.dnedev.favorite.places.data.VenuesApiResponse
import com.dnedev.favorite.places.data.covertToVenueItemUiModel
import com.dnedev.favorite.places.di.modules.RepositoryModule
import com.dnedev.favorite.places.ui.venues.VenueItemUiModel
import com.dnedev.favorite.places.utils.*
import retrofit2.Response
import javax.inject.Inject

class VenuesRepository @Inject constructor(
    @RepositoryModule.VenuesRemoteSourceData private val remoteSource: RemoteSource
) {
    interface RemoteSource {
        suspend fun getVenues(queryParameters: Map<String, String>): Response<VenuesApiResponse>
    }

    suspend fun getVenues(
        nearCity: String,
        radius: Int,
        categoryId: String
    ): Pair<List<VenueItemUiModel>?, String?> {
        return remoteSource.getVenues(
            mapOf(
                CLIENT_ID_QUERY_PARAMETER to BuildConfig.CLIENT_ID,
                CLIENT_SECRET_QUERY_PARAMETER to BuildConfig.CLIENT_SECRET,
                NEAR_CITY_QUERY_PARAMETER to nearCity,
                RADIUS_QUERY_PARAMETER to radius.toString(),
                VERSION_ID_QUERY_PARAMETER to BuildConfig.API_VERSION,
                CATEGORY_ID_QUERY_PARAMETER to categoryId
            )
        ).body()?.venuesResponse?.venues?.map {
            it.covertToVenueItemUiModel()
        } to null

    }
}