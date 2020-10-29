package com.dnedev.favorite.places.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dnedev.favorite.places.data.venues.Venue

@Database(entities = [Venue::class], version = 1, exportSchema = false)
abstract class FavoritePlacesDatabase : RoomDatabase() {

    abstract val venueDatabaseDao: VenueDatabaseDao
}
