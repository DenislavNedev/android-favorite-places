package com.dnedev.favorite.places.di

import com.dnedev.favorite.places.FavoritePlacesApplication
import com.dnedev.favorite.places.di.modules.AppModule
import com.dnedev.favorite.places.di.modules.ApplicationBindingModule
import com.dnedev.favorite.places.di.modules.NetworkModule
import com.dnedev.favorite.places.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        ViewModelBuilderModule::class,
        ApplicationBindingModule::class,
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface AppComponent : AndroidInjector<FavoritePlacesApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: FavoritePlacesApplication): Builder

        fun build(): AppComponent
    }
}