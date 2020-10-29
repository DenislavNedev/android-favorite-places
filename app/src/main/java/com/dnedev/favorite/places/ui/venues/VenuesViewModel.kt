package com.dnedev.favorite.places.ui.venues

import android.app.Application
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.data.map.ShowOnMapItem
import com.dnedev.favorite.places.data.venues.convertToVenueItemUiModel
import com.dnedev.favorite.places.navigation.GraphNav
import com.dnedev.favorite.places.navigation.NavigateTo
import com.dnedev.favorite.places.repositories.venues.VenuesRepository
import com.dnedev.favorite.places.utils.*
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class VenuesViewModel @Inject constructor(
    application: Application,
    private val venuesRepository: VenuesRepository
) : AndroidViewModel(application),
    VenueItemPresenter,
    LifecycleObserver {

    private val _uiModel = MediatorLiveData<VenuesUiModel>().apply {
        value = VenuesUiModel()
    }
    val uiModel: LiveData<VenuesUiModel>
        get() = _uiModel

    private val _navigation = SingleLiveEvent<NavigateTo>()
    val navigation: LiveData<NavigateTo>
        get() = _navigation

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun initViewModel() {
        _uiModel.value = VenuesUiModel()
        viewModelScope.launch {
            if (getApplication<Application>().applicationContext.isNetworkAvailable()) {
                viewModelScope.launch {
                    initVenues(SUPERMARKET_CATEGORY_ID)
                    initVenues(RESTAURANT_CATEGORY_ID)
                    getFavorites()
                    checkAreAllItemsFavorite(SUPERMARKET_CATEGORY_ID)
                    checkAreAllItemsFavorite(RESTAURANT_CATEGORY_ID)
                }
            } else {
                loadVenuesOffline()
                checkAreAllItemsFavorite(SUPERMARKET_CATEGORY_ID)
                checkAreAllItemsFavorite(RESTAURANT_CATEGORY_ID)
                getFavorites()
            }
        }
    }

    private suspend fun initVenues(categoryId: String) {
        try {
            handleVenueResponse(
                venuesRepository.getVenues(
                    POMORIE_NEAR_CITY,
                    RADIUS,
                    categoryId
                ), categoryId
            )
        } catch (exception: Exception) {
            _uiModel.value?.errorMessageId = R.string.unexpected_error
        }
    }

    private suspend fun loadVenuesOffline() {
        _uiModel.addSource(venuesRepository.getAllVenues()) { favoriteVenues ->
            favoriteVenues.map { it.convertToVenueItemUiModel() }.filter { venue ->
                _uiModel.value?.listOfVenues?.let {
                    !it.contains(venue)
                } ?: false
            }.let { newVenues ->
                showVenuesOffline(
                    newVenues.filter { it.categoryId == SUPERMARKET_CATEGORY_ID },
                    SUPERMARKET_CATEGORY_ID
                )
                showVenuesOffline(
                    newVenues.filter { it.categoryId == RESTAURANT_CATEGORY_ID },
                    RESTAURANT_CATEGORY_ID
                )
            }

        }
    }

    private fun showVenuesOffline(venues: List<VenueItemUiModel>, categoryId: String) {
        if (venues.isNotEmpty()) {
            _uiModel.value = _uiModel.value?.apply {
                this.listOfVenues = listOfVenues + createVenueHeaderItem(categoryId) + venues
            }
        }
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

    private fun handleVenueResponse(
        response: Pair<List<VenueItemUiModel>?, Int?>,
        categoryId: String
    ) {
        when {
            response.first != null -> {
                _uiModel.value = _uiModel.value?.apply {
                    (response.first ?: emptyList()).let { responseList ->
                        if (responseList.isNotEmpty()) {
                            this.listOfVenues =
                                listOfVenues + createVenueHeaderItem(categoryId) + responseList
                        }
                    }
                }
            }
            response.second != null -> {
                _uiModel.value?.errorMessageId = response.second ?: INVALID_RESOURCE
            }
            else -> _uiModel.value?.errorMessageId = R.string.unexpected_error
        }
    }

    private suspend fun checkAreAllItemsFavorite(categoryId: String) {
        _uiModel.addSource(venuesRepository.getVenuesByCategory(categoryId)) { currentVenues ->
            _uiModel.value?.listOfVenues?.let { allVenues ->
                if (allVenues.isNotEmpty()) {
                    allVenues.filterIsInstance<VenueItemUiModel>()
                        .filter { it.categoryId == categoryId }.let { categoryVenues ->
                            if (categoryVenues.isNotEmpty()) {
                                categoryVenues.size.let { numberOfAllVenues ->
                                    allVenues.filterIsInstance<VenueHeaderUiModel>()
                                        .first { it.categoryId == categoryId }.areAllVenuesFavorite =
                                        currentVenues.size == numberOfAllVenues && currentVenues.isNotEmpty() && numberOfAllVenues != 0
                                }
                            }
                        }
                }
            }
        }
    }

    override fun addAsFavorite(venueListItem: VenueListItem) {
        when (venueListItem) {
            is VenueItemUiModel -> addVenueAsFavorite(venueListItem)
            is VenueHeaderUiModel -> addAllToFavorite(venueListItem)
        }
    }

    private fun addVenueAsFavorite(venueItemUiModel: VenueItemUiModel) {
        with(venueItemUiModel) {
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

    private fun addAllToFavorite(venueHeaderUiModel: VenueHeaderUiModel) {
        with(venueHeaderUiModel) {

            if (areAllVenuesFavorite) {
                _uiModel.value?.listOfVenues?.filterIsInstance<VenueItemUiModel>()
                    ?.filter { it.categoryId == categoryId && it.isAddedAsFavorite }
                    ?.let { favoriteVenues ->
                        favoriteVenues.map { it.convertToVenue() }.let {
                            viewModelScope.launch {
                                venuesRepository.deleteVenues(it)
                            }
                        }
                        favoriteVenues.forEach { it.isAddedAsFavorite = false }
                        areAllVenuesFavorite = false
                    }
            } else {
                _uiModel.value?.listOfVenues?.filterIsInstance<VenueItemUiModel>()
                    ?.filter { it.categoryId == categoryId && !it.isAddedAsFavorite }
                    ?.map { it.convertToVenue() }?.let {
                        viewModelScope.launch {
                            venuesRepository.insertVenues(it)
                        }
                    }
                areAllVenuesFavorite = true
            }
        }
    }

    private fun createVenueHeaderItem(categoryId: String) =
        VenueHeaderUiModel(getNameForCategoryById(categoryId), categoryId)

    private fun getNameForCategoryById(categoryId: String) = when (categoryId) {
        SUPERMARKET_CATEGORY_ID -> getApplication<Application>().getString(R.string.supermarkets)
        else -> getApplication<Application>().getString(R.string.restaurants)
    }

    override fun showOnMap(venueListItem: VenueListItem) {
        if (venueListItem is VenueItemUiModel) {
            viewModelScope.launch {
                convertVenueItemToShowOnMapJson(venueListItem).let {
                    _navigation.value = GraphNav(
                        R.id.action_venuesFragment_to_mapFragment,
                        bundleOf(SHOW_ON_MAP_ITEM_KEY to it)
                    )
                }
            }
        }
    }

    private suspend fun convertVenueItemToShowOnMapJson(venueItemUiModel: VenueItemUiModel) =
        withContext(Dispatchers.IO) {
            Moshi.Builder().build().adapter(ShowOnMapItem::class.java)
                .toJson(venueItemUiModel.convertToShowOnMapItem())
        }

    companion object {
        const val INVALID_INDEX = -1
    }
}