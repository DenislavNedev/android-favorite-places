package com.dnedev.favorite.places.di.modules

import androidx.lifecycle.ViewModel
import com.dnedev.favorite.places.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    //TODO Uncomment this when viewModel is needed
//    @Binds
//    @IntoMap
//    @ViewModelKey(PlacesViewModel::class)
//    abstract fun bindPlacesViewModel(viewModel: PlacesViewModel): ViewModel
}