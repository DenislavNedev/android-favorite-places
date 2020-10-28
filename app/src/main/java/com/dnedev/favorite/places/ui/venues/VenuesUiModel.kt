package com.dnedev.favorite.places.ui.venues

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlin.properties.Delegates
import androidx.databinding.library.baseAdapters.BR

class VenuesUiModel : BaseObservable() {
    @get:Bindable
    var listOfSupermarkets: List<VenueItemUiModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyPropertyChanged(BR.listOfSupermarkets)
    }

    @get:Bindable
    var listOfRestaurants: List<VenueItemUiModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyPropertyChanged(BR.listOfRestaurants)
    }
}