package com.dnedev.favorite.places.ui.venues

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.dnedev.favorite.places.data.venues.Venue
import kotlin.properties.Delegates

data class VenueItemUiModel(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val categoryId: String
) : BaseObservable(), VenueListItem {
    @get:Bindable
    var isAddedAsFavorite: Boolean by Delegates.observable(false) { _, _, _ ->
        notifyPropertyChanged(BR.addedAsFavorite)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VenueItemUiModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }
}

fun VenueItemUiModel.convertToVenue() = Venue(
    id = id,
    categoryId = categoryId,
    name = name,
    latitude = latitude,
    longitude = longitude
)