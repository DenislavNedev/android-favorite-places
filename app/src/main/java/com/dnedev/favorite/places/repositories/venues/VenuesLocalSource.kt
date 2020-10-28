package com.dnedev.favorite.places.repositories.venues

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
}