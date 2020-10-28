package com.dnedev.favorite.places.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.dnedev.favorite.places.repositories.venues.VenuesRepository
import com.dnedev.favorite.places.ui.venues.VenueItemUiModel
import com.dnedev.favorite.places.utils.POMORIE_NEAR_CITY
import com.dnedev.favorite.places.utils.RADIUS
import com.dnedev.favorite.places.utils.RESTAURANT_CATEGORY_ID
import com.dnedev.favorite.places.utils.SUPERMARKET_CATEGORY_ID
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
        viewModelScope.launch {
            initVenues(RESTAURANT_CATEGORY_ID)
        }
    }

    override fun loadSupermarkets() {
        viewModelScope.launch {
            initVenues(SUPERMARKET_CATEGORY_ID)
        }
    }
}