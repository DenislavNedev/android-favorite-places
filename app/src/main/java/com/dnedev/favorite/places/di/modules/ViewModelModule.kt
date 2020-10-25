package com.dnedev.favorite.places.di.modules

import androidx.lifecycle.ViewModel
import com.dnedev.favorite.places.di.ViewModelKey
import com.dnedev.favorite.places.ui.venues.VenuesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(VenuesViewModel::class)
    abstract fun bindVenuesViewModel(viewModel: VenuesViewModel): ViewModel
}