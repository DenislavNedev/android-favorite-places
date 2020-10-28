package com.dnedev.favorite.places.ui.venues

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.data.venues.convertToVenueItemUiModel
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
) : AndroidViewModel(application),
    VenueItemPresenter {

    private val _uiModel = MediatorLiveData<VenuesUiModel>().apply {
        value = VenuesUiModel()
    }
    val uiModel: LiveData<VenuesUiModel>
        get() = _uiModel

    //TODO check for internet
    init {
        viewModelScope.launch {
            initVenues(SUPERMARKET_CATEGORY_ID)
            initVenues(RESTAURANT_CATEGORY_ID)
            getFavorites()
        }
    }

    //TODO add no venues state
    //TODO try and catch

    private suspend fun initVenues(categoryId: String) {
        handleVenueResponse(
            venuesRepository.getVenues(
                POMORIE_NEAR_CITY,
                RADIUS,
                categoryId
            ), categoryId
        )
    }

    private suspend fun getFavorites() {
        _uiModel.addSource(venuesRepository.getAllVenues()) { favoriteVenues ->
            _uiModel.value?.listOfVenues?.let { currentListOfVenues ->
                favoriteVenues.map { it.convertToVenueItemUiModel() }.forEach { favoriteVenue ->
                    currentListOfVenues.indexOf(favoriteVenue).let { index ->
                        if (index != INVALID_INDEX) {
                            currentListOfVenues[index].let { currentItem ->
                                if (currentItem is VenueItemUiModel) {
                                    currentItem.isAddedAsFavorite = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //TODO handle error response
    private fun handleVenueResponse(
        response: Pair<List<VenueItemUiModel>?, String?>,
        categoryId: String
    ) {
        _uiModel.value = _uiModel.value?.apply {
            (response.first ?: emptyList()).let { responseList ->
                if (responseList.isNotEmpty()) {
                    this.listOfVenues =
                        listOfVenues + createVenueHeaderItem(categoryId) + responseList
                }
            }
        }
    }

    override fun addAsFavorite(venueListItem: VenueListItem) {
        when (venueListItem) {
            is VenueItemUiModel -> {
                with(venueListItem) {
                    this.convertToVenue().let {
                        viewModelScope.launch {
                            if (isAddedAsFavorite) {
                                venuesRepository.deleteVenue(it)
                                isAddedAsFavorite = false
                            } else {
                                venuesRepository.addVenueAsFavorite(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createVenueHeaderItem(categoryId: String) =
        VenueHeaderUiModel(getNameForCategoryById(categoryId), categoryId)

    private fun getNameForCategoryById(categoryId: String) = when (categoryId) {
        SUPERMARKET_CATEGORY_ID -> getApplication<Application>().getString(R.string.supermarkets)
        else -> getApplication<Application>().getString(R.string.supermarkets)
    }

    companion object {
        const val INVALID_INDEX = -1
    }
}