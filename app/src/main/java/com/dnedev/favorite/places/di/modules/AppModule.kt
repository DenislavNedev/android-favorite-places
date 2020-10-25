package com.dnedev.favorite.places.di.modules

import android.app.Application
import com.dnedev.favorite.places.FavoritePlacesApplication
import dagger.Module
import dagger.Provides

@Module
object AppModule {
    @Provides
    fun provideContext(application: FavoritePlacesApplication): Application = application
}
