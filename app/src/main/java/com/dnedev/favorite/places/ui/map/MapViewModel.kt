package com.dnedev.favorite.places.ui.map

import android.app.Application
import androidx.lifecycle.*
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.data.map.ShowOnMapItem
import com.dnedev.favorite.places.data.map.convertToVenueItemUiModel
import com.dnedev.favorite.places.data.venues.convertToVenueItemUiModel
import com.dnedev.favorite.places.repositories.venues.VenuesRepository
import com.dnedev.favorite.places.ui.venues.VenueItemUiModel
import com.dnedev.favorite.places.ui.venues.convertToShowOnMapItem
import com.dnedev.favorite.places.ui.venues.convertToVenue
import com.dnedev.favorite.places.utils.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class MapViewModel @Inject constructor(
    application: Application,
    private val venuesRepository: VenuesRepository
) : AndroidViewModel(application),
    MapPresenter,
    GoogleMap.OnInfoWindowClickListener,
    LifecycleObserver {

    private val _uiModel = MediatorLiveData<MapUiModel>().apply {
        value = MapUiModel()
    }
    val uiModel: LiveData<MapUiModel>
        get() = _uiModel

    private suspend fun initVenues(categoryId: String) {
        try {
            handleVenueResponse(
                venuesRepository.getVenues(
                    POMORIE_NEAR_CITY,
                    RADIUS,
                    categoryId
                )
            )
        } catch (exception: Exception) {
            //Do nothing since the criteria in the assignment is to show empty map, when no results are found
        }
    }

    private fun handleVenueResponse(
        response: Pair<List<VenueItemUiModel>?, Int?>
    ) {
        when {
            response.first != null -> {
                _uiModel.value = _uiModel.value?.apply {
                    (response.first ?: emptyList()).let { responseList ->
                        if (responseList.isNotEmpty()) {
                            this.listOfVenues = responseList
                            this.listOfMarkers = responseList.map { venue ->
                                createMapMarker(venue)
                            }
                        }
                    }
                }
            }
            else -> {
                //Do nothing since the criteria in the assignment is to show empty map, when no results are found
            }
        }
    }

    private fun createMapMarker(venue: VenueItemUiModel) = MarkerOptions().position(
        LatLng(
            venue.latitude,
            venue.longitude
        )
    ).title(venue.name)
        .snippet(
            if (venue.isAddedAsFavorite) getApplication<Application>().getString(R.string.tap_to_remove_from_favorites)
            else getApplication<Application>().getString(R.string.tap_to_add_to_favorite)
        )

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
                getFavorites()
            } else {
                loadVenuesOffline(categoryId)
                getFavorites()
            }
        }
    }

    private suspend fun loadVenuesOffline(categoryId: String) {
        _uiModel.addSource(venuesRepository.getAllVenues()) { favoriteVenues ->
            favoriteVenues.map { it.convertToVenueItemUiModel() }.filter { venue ->
                _uiModel.value?.listOfVenues?.let {
                    !it.contains(venue)
                } ?: false
            }.let { newVenues ->
                newVenues.filter { it.categoryId == categoryId }.let { venues ->
                    _uiModel.value = _uiModel.value?.apply {
                        this.listOfVenues = this.listOfVenues + venues
                        this.listOfMarkers = venues.map { venue ->
                            createMapMarker(venue)
                        }
                    }
                }
            }
        }
    }

    private suspend fun getFavorites() {
        _uiModel.addSource(venuesRepository.getAllVenues()) { favoriteVenues ->
            _uiModel.value?.listOfVenues?.let { currentListOfVenues ->
                favoriteVenues.map { it.convertToVenueItemUiModel() }.forEach { favoriteVenue ->
                    currentListOfVenues.indexOf(favoriteVenue).let { index ->
                        if (index != INVALID_INDEX) {
                            currentListOfVenues[index].isAddedAsFavorite = true
                        }
                    }
                }
                _uiModel.value = _uiModel.value?.apply {
                    this.listOfMarkers = currentListOfVenues.map { venue ->
                        createMapMarker(venue)
                    }
                }
            }
        }
    }

    private fun addVenueAsFavorite(venueItemUiModel: VenueItemUiModel) {
        _uiModel.value?.listOfVenues?.let {
            with(it[it.indexOf(venueItemUiModel)]) {
                this.convertToVenue().let {
                    viewModelScope.launch {
                        isAddedAsFavorite = if (isAddedAsFavorite) {
                            venuesRepository.deleteVenue(it)
                            false
                        } else {
                            venuesRepository.addVenueAsFavorite(it)
                            true
                        }
                    }
                }
            }
        }
    }

    override fun onInfoWindowClick(marker: Marker?) {
        _uiModel.value?.listOfVenues?.firstOrNull { venueItemUiModel -> venueItemUiModel.name == marker?.title }
            ?.let {
                addVenueAsFavorite(it)
            }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun clearUiModel() {
        _uiModel.value = MapUiModel()
    }

    fun showOnMap(showOnMapItemJson: String): LatLng? {
        val currentItem: VenueItemUiModel? = null

        viewModelScope.launch {
            convertShowOnMapItemToVenueItemUiModel(showOnMapItemJson)?.let {
                _uiModel.value = _uiModel.value?.apply {
                    this.listOfVenues = listOf(it)
                    this.listOfMarkers = this.listOfVenues.map { venue ->
                        createMapMarker(venue)
                    }
                }
                getFavorites()
            }
        }
        return currentItem?.let {
            LatLng(
                currentItem.latitude,
                currentItem.longitude
            )
        }
    }

    private suspend fun convertShowOnMapItemToVenueItemUiModel(showOnMapItemJson: String) =
        withContext(Dispatchers.IO) {
            Moshi.Builder().build().adapter(ShowOnMapItem::class.java)
                .fromJson(showOnMapItemJson)?.convertToVenueItemUiModel()
        }

    companion object {
        const val INVALID_INDEX = -1
    }
}