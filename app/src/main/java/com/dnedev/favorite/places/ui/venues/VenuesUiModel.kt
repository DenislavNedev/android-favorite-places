package com.dnedev.favorite.places.ui.venues

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlin.properties.Delegates
import androidx.databinding.library.baseAdapters.BR

class VenuesUiModel : BaseObservable() {
    @get:Bindable
    var listOfVenues: List<VenueListItem> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyPropertyChanged(BR.listOfVenues)
    }
}