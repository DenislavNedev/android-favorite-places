package com.dnedev.favorite.places.di.modules

import com.dnedev.favorite.places.ui.main.MainActivity
import com.dnedev.favorite.places.ui.map.MapFragment
import com.dnedev.favorite.places.ui.venues.VenuesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ApplicationBindingModule {

    @ContributesAndroidInjector
    fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    fun mapFragment(): MapFragment

    @ContributesAndroidInjector
    fun venuesFragment(): VenuesFragment
}