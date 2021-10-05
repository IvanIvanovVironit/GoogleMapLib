package com.example.library.di


import android.content.Context
import com.example.library.api.GoogleMapApi
import com.example.library.database.MapDatabase
import com.example.library.database.PlaceDao
import com.example.library.mapper.PlaceModelMapperImpl
import com.example.library.repository.PlaceRepository
import com.example.library.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(GoogleMapApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideGoogleApi(retrofit: Retrofit): GoogleMapApi =
        retrofit.create(GoogleMapApi::class.java)


    // Map database
    @Singleton
    @Provides
    fun getRoomDbInstance(@ApplicationContext context: Context): MapDatabase {
        return MapDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun getPlaceDao(database: MapDatabase): PlaceDao {
        return database.placeDao()
    }

    @Singleton
    @Provides
    fun getRepositoryPlace(roomDatabase: MapDatabase, mapperImpl: PlaceModelMapperImpl): Repository {
        return PlaceRepository(roomDatabase.placeDao(), mapperImpl)
    }
}