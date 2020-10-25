package com.dnedev.favorite.places.di.modules

import com.dnedev.favorite.places.network.VenuesApi
import com.dnedev.favorite.places.repositories.venues.VenuesRemoteSource
import com.dnedev.favorite.places.repositories.venues.VenuesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class VenuesRemoteSourceData

    @Singleton
    @VenuesRemoteSourceData
    @Provides
    fun providePlacesRemoteSource(placesApi: VenuesApi): VenuesRepository.RemoteSource =
        VenuesRemoteSource(placesApi)
}