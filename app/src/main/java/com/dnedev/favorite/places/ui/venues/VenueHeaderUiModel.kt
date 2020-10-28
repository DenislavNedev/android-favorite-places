package com.dnedev.favorite.places.ui.venues

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlin.properties.Delegates

data class VenueHeaderUiModel(
    val categoryName: String,
    val categoryId: String
) : BaseObservable(),
    VenueListItem {
    @get:Bindable
    var areAllVenuesFavorite: Boolean by Delegates.observable(false) { _, _, _ ->
        notifyPropertyChanged(BR.areAllVenuesFavorite)
    }
}