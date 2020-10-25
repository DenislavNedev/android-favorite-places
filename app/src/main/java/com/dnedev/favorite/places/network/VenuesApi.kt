package com.dnedev.favorite.places.network

import com.dnedev.favorite.places.data.VenuesApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap
import javax.inject.Inject

interface VenuesApiService {
    @GET("venues/search")
    suspend fun getVenues(@QueryMap(encoded = true) parameters: Map<String, String>): Response<VenuesApiResponse>
}

class VenuesApi @Inject constructor(private val apiService: VenuesApiService) {

    suspend fun getVenues(queryParameters: Map<String, String>) =
        withContext(Dispatchers.IO) {
            apiService.getVenues(queryParameters)
        }
}
