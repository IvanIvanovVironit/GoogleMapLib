package com.example.library.di


import com.example.library.api.GoogleMapApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    // Retrofit
    @Provides
    @Singleton
    fun provideRetrofitLib(): Retrofit =
        Retrofit.Builder()
            .baseUrl(GoogleMapApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGoogleApiLib(retrofit: Retrofit): GoogleMapApi =
        retrofit.create(GoogleMapApi::class.java)
}