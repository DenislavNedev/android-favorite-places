package com.dnedev.favorite.places.repositories.venues

import androidx.lifecycle.LiveData
import com.dnedev.favorite.places.data.venues.Venue
import com.dnedev.favorite.places.database.VenueDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VenuesLocalSource @Inject constructor(private val venuesDatabaseDao: VenueDatabaseDao) :
    VenuesRepository.LocalSource {

    override suspend fun insert(venue: Venue) {
        withContext(Dispatchers.IO) {
            venuesDatabaseDao.insert(venue)
        }
    }

    override suspend fun delete(venue: Venue) {
        withContext(Dispatchers.IO) {
            venuesDatabaseDao.delete(venue)
        }
    }

    override suspend fun getAllVenues() = withContext(Dispatchers.IO) {
        venuesDatabaseDao.getAllVenues()
    }

    override suspend fun deleteVenues(venues: List<Venue>) {
        withContext(Dispatchers.IO) {
            venuesDatabaseDao.deleteVenues(venues)
        }
    }

    override suspend fun insertVenues(venues: List<Venue>) {
        withContext(Dispatchers.IO) {
            venuesDatabaseDao.insertVenues(venues)
        }
    }

    override suspend fun getAllVenuesByCategory(categoryId: String): LiveData<List<Venue>> =
        withContext(Dispatchers.IO) {
            venuesDatabaseDao.getVenuesByCategory(categoryId)
        }
}