package com.dnedev.favorite.places.ui.venues

interface VenueItemPresenter {
    fun addAsFavorite(venueListItem: VenueListItem)
    fun showOnMap(venueListItem: VenueListItem)
}