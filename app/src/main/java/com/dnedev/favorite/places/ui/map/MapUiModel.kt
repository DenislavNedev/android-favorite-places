package com.dnedev.favorite.places.ui.map

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.dnedev.favorite.places.ui.venues.VenueItemUiModel
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.properties.Delegates

class MapUiModel : BaseObservable() {
    @get:Bindable
    var listOfVenues: List<VenueItemUiModel> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyPropertyChanged(BR.listOfVenues)
    }

    @get:Bindable
    var listOfMarkers: List<MarkerOptions> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyPropertyChanged(BR.listOfMarkers)
    }
}