package com.dnedev.favorite.places.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.dnedev.favorite.places.data.venues.convertToVenueItemUiModel
import com.dnedev.favorite.places.repositories.venues.VenuesRepository
import com.dnedev.favorite.places.ui.venues.VenueItemUiModel
import com.dnedev.favorite.places.utils.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel @Inject constructor(
    application: Application,
    private val venuesRepository: VenuesRepository
) : AndroidViewModel(application),
    MapPresenter {

    private val _uiModel = MediatorLiveData<MapUiModel>().apply {
        value = MapUiModel()
    }
    val uiModel: LiveData<MapUiModel>
        get() = _uiModel

    private suspend fun initVenues(categoryId: String) {
        handleVenueResponse(
            venuesRepository.getVenues(
                POMORIE_NEAR_CITY,
                RADIUS,
                categoryId
            )
        )
    }

    private fun handleVenueResponse(
        response: Pair<List<VenueItemUiModel>?, String?>
    ) {
        _uiModel.value = _uiModel.value?.apply {
            (response.first ?: emptyList()).let { responseList ->
                if (responseList.isNotEmpty()) {
                    this.listOfVenues = responseList
                    this.listOfMarkers = responseList.map { venue ->
                        MarkerOptions().position(
                            LatLng(
                                venue.latitude,
                                venue.longitude
                            )
                        ).title(venue.name)
                    }
                }
            }
        }
    }

    override fun loadRestaurants() {
        loadVenues(RESTAURANT_CATEGORY_ID)
    }

    override fun loadSupermarkets() {
        loadVenues(SUPERMARKET_CATEGORY_ID)
    }

    private fun loadVenues(categoryId: String) {
        viewModelScope.launch {
            if (getApplication<Application>().applicationContext.isNetworkAvailable()) {
                initVenues(categoryId)
            } else {
                loadVenuesOffline(categoryId)
            }
        }
    }

    private suspend fun loadVenuesOffline(categoryId: String) {
        _uiModel.addSource(venuesRepository.getAllVenues()) { favoriteVenues ->
            favoriteVenues.filter { it.categoryId == categoryId }
                .map { it.convertToVenueItemUiModel() }.let { venues ->
                    _uiModel.value = _uiModel.value?.apply {
                        this.listOfVenues = venues
                        this.listOfMarkers = venues.map { venue ->
                            MarkerOptions().position(
                                LatLng(
                                    venue.latitude,
                                    venue.longitude
                                )
                            ).title(venue.name)
                        }
                    }
                }
        }
    }
}