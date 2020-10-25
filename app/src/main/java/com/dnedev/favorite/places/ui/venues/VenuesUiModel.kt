package com.dnedev.favorite.places.ui.venues

import androidx.databinding.BaseObservable

class VenuesUiModel : BaseObservable() {
    var listOfSupermarkets: List<VenueItemUiModel> = emptyList()
    var listOfRestaurants: List<VenueItemUiModel> = emptyList()
}