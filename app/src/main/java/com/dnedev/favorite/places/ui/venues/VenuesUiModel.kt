package com.dnedev.favorite.places.ui.venues

import androidx.annotation.StringRes
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlin.properties.Delegates
import androidx.databinding.library.baseAdapters.BR
import com.dnedev.favorite.places.R

class VenuesUiModel : BaseObservable() {
    @get:Bindable
    var listOfVenues: List<VenueListItem> by Delegates.observable(emptyList()) { _, _, _ ->
        isVenuesListEmpty = listOfVenues.isEmpty()
        notifyPropertyChanged(BR.listOfVenues)
    }

    @get:Bindable
    var isVenuesListEmpty: Boolean by Delegates.observable(true) { _, _, _ ->
        notifyPropertyChanged(BR.venuesListEmpty)
    }

    @get:Bindable
    var errorMessageId: Int by Delegates.observable(R.string.sorry_no_results_found_for_this_area) { _, _, _ ->
        notifyPropertyChanged(BR.errorMessageId)
    }
}