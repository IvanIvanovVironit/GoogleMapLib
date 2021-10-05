package com.example.library.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.library.database.PlaceDao
import com.example.library.database.PlaceEntity
import com.example.library.mapper.PlaceModelMapperImpl
import com.example.library.model.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.Q)
class PlaceRepository  @Inject constructor(
    val PlaceDao: PlaceDao,
    val mapper: PlaceModelMapperImpl
    ) : Repository {

    override fun readAll(): Flow<List<Place>> {
        return PlaceDao.readAllData().map { it.map(mapper::fromEntity) }
    }

    override suspend fun delete(place: Place) {
        return PlaceDao.delete(mapper.toEntity(place))
    }

    override suspend fun update(place: Place) {
        return PlaceDao.update(mapper.toEntity(place))
    }

    override suspend fun insert(place: Place) {
            return PlaceDao.insert(mapper.toEntity(place))
        }

    override suspend fun deleteAll() {
        PlaceDao.deleteAll()
    }

    override suspend fun insertAll(list: List<Place>) {
        val placeEntity: List<PlaceEntity> = list.map { mapper.toEntity(it) }
        PlaceDao.insertAll(placeEntity)
    }
}