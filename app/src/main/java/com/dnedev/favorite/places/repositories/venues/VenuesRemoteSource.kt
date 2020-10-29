package com.dnedev.favorite.places.repositories.venues

import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.data.venues.VenuesApiResponse
import com.dnedev.favorite.places.network.VenuesApi
import com.dnedev.favorite.places.utils.exception.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class VenuesRemoteSource @Inject constructor(private val venuesApi: VenuesApi) :
    VenuesRepository.RemoteSource {

    override suspend fun getVenues(queryParameters: Map<String, String>): Response<VenuesApiResponse> =
        withContext(Dispatchers.IO) {
            venuesApi.getVenues(queryParameters).let {
                if (it.isSuccessful) it
                else throw ResponseException(R.string.unexpected_error)
            }
        }
}