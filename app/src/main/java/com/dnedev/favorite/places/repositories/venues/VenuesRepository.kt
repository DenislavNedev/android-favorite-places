package com.dnedev.favorite.places.repositories.venues

import androidx.lifecycle.LiveData
import com.dnedev.favorite.places.BuildConfig
import com.dnedev.favorite.places.data.venues.Venue
import com.dnedev.favorite.places.data.venues.VenuesApiResponse
import com.dnedev.favorite.places.data.venues.covertToVenueItemUiModel
import com.dnedev.favorite.places.di.modules.RepositoryModule
import com.dnedev.favorite.places.ui.venues.VenueItemUiModel
import com.dnedev.favorite.places.utils.*
import com.dnedev.favorite.places.utils.exception.ResponseException
import retrofit2.Response
import javax.inject.Inject

class VenuesRepository @Inject constructor(
    @RepositoryModule.VenuesRemoteSourceData private val remoteSource: RemoteSource,
    @RepositoryModule.VenuesLocalSourceData private val localSource: LocalSource
) {
    interface RemoteSource {
        @Throws(ResponseException::class)
        suspend fun getVenues(queryParameters: Map<String, String>): Response<VenuesApiResponse>
    }

    interface LocalSource {
        suspend fun insert(venue: Venue)
        suspend fun delete(venue: Venue)
        suspend fun getAllVenues(): LiveData<List<Venue>>
        suspend fun deleteVenues(venues: List<Venue>)
        suspend fun insertVenues(venues: List<Venue>)
        suspend fun getAllVenuesByCategory(categoryId: String): LiveData<List<Venue>>
    }

    suspend fun getVenues(
        nearCity: String,
        radius: Int,
        categoryId: String
    ): Pair<List<VenueItemUiModel>?, Int?> {
        try {
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
                it.covertToVenueItemUiModel(categoryId)
            } to null
        } catch (responseException: ResponseException) {
            return null to responseException.stringResource
        }
    }

    suspend fun addVenueAsFavorite(venue: Venue) {
        localSource.insert(venue)
    }

    suspend fun deleteVenue(venue: Venue) {
        localSource.delete(venue)
    }

    suspend fun getAllVenues() = localSource.getAllVenues()

    suspend fun deleteVenues(venues: List<Venue>) {
        localSource.deleteVenues(venues)
    }

    suspend fun insertVenues(venues: List<Venue>) {
        localSource.insertVenues(venues)
    }

    suspend fun getVenuesByCategory(categoryId: String) =
        localSource.getAllVenuesByCategory(categoryId)
}