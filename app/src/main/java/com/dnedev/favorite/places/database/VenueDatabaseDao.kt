package com.dnedev.favorite.places.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dnedev.favorite.places.data.venues.Venue

@Dao
interface VenueDatabaseDao {
    @Insert
    fun insert(venue: Venue)

    @Delete
    fun delete(venue: Venue)

    @Delete
    fun update(venue: Venue)

    @Query("SELECT * FROM venues")
    fun getAllVenues(): LiveData<List<Venue>>

    @Query("SELECT * FROM venues WHERE category_id=:categoryId")
    fun getVenuesByCategory(categoryId: String): LiveData<List<Venue>>
}
