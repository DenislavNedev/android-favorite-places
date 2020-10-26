package com.dnedev.favorite.places.repositories.venues

import com.dnedev.favorite.places.data.venues.VenuesApiResponse
import com.dnedev.favorite.places.network.VenuesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class VenuesRemoteSource @Inject constructor(private val venuesApi: VenuesApi) :
    VenuesRepository.RemoteSource {

    //TODO throw exception if not successfull
    override suspend fun getVenues(queryParameters: Map<String, String>): Response<VenuesApiResponse> =
        withContext(Dispatchers.IO) {
            venuesApi.getVenues(queryParameters)
        }
}