package com.dnedev.favorite.places

import androidx.databinding.DataBindingUtil
import com.dnedev.favorite.places.di.DaggerAppComponent
import com.dnedev.favorite.places.ui.binding.AppDataBindingComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class FavoritePlacesApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this@FavoritePlacesApplication).build()
}