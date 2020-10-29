package com.dnedev.favorite.places.di.modules

import com.dnedev.favorite.places.BuildConfig.API_URL
import com.dnedev.favorite.places.network.VenuesApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .retryOnConnectionFailure(true)
        .build()

    @Singleton
    @Provides
    @Named("favoritePlacesRetrofit")
    fun provideRetrofitFavoritePlaces(
        moshi: Moshi,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(API_URL)
        .client(client)
        .build()

    @Singleton
    @Provides
    fun providePlacesApiService(@Named("favoritePlacesRetrofit") retrofit: Retrofit): VenuesApiService =
        retrofit.create(VenuesApiService::class.java)
}