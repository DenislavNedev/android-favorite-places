package com.dnedev.favorite.places.ui.venues

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dnedev.favorite.places.repositories.venues.VenuesRepository
import com.dnedev.favorite.places.utils.POMORIE_NEAR_CITY
import com.dnedev.favorite.places.utils.RADIUS
import com.dnedev.favorite.places.utils.RESTAURANT_CATEGORY_ID
import com.dnedev.favorite.places.utils.SUPERMARKET_CATEGORY_ID
import kotlinx.coroutines.launch
import javax.inject.Inject

class VenuesViewModel @Inject constructor(
    application: Application,
    private val venuesRepository: VenuesRepository
) : AndroidViewModel(application) {

    private val _uiModel = MutableLiveData<VenuesUiModel>().apply {
        value = VenuesUiModel()
    }
    val uiModel: LiveData<VenuesUiModel>
        get() = _uiModel

    init {
        viewModelScope.launch {
            initRestaurants()
            initSupermarkets()
        }
    }

    //TODO try and catch
    private suspend fun initRestaurants() {
        handleVenueResponse(
            venuesRepository.getVenues(
                POMORIE_NEAR_CITY,
                RADIUS,
                RESTAURANT_CATEGORY_ID
            ), RESTAURANT_CATEGORY_ID
        )
    }

    private suspend fun initSupermarkets() {
        handleVenueResponse(
            venuesRepository.getVenues(
                POMORIE_NEAR_CITY,
                RADIUS,
                SUPERMARKET_CATEGORY_ID
            ), SUPERMARKET_CATEGORY_ID
        )
    }

    //TODO handle error response
    private fun handleVenueResponse(
        response: Pair<List<VenueItemUiModel>?, String?>,
        categoryId: String
    ) {
        when (categoryId) {
            RESTAURANT_CATEGORY_ID -> {
                _uiModel.value = _uiModel.value?.apply {
                    listOfRestaurants = response.first ?: emptyList()
                }
            }
            SUPERMARKET_CATEGORY_ID -> {
                _uiModel.value = _uiModel.value?.apply {
                    listOfSupermarkets = response.first ?: emptyList()
                }
            }
        }
    }
}