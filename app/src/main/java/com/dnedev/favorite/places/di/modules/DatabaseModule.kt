package com.dnedev.favorite.places.di.modules

import android.app.Application
import androidx.room.Room
import com.dnedev.favorite.places.R
import com.dnedev.favorite.places.database.FavoritePlacesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(application: Application): FavoritePlacesDatabase = Room.databaseBuilder(
        application.applicationContext,
        FavoritePlacesDatabase::class.java,
        application.getString(R.string.database_name)
    )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun providesVenueDatabaseDao(database: FavoritePlacesDatabase) = database.venueDatabaseDao
}
