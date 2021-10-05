package com.example.library.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.library.database.PlaceEntity
import com.example.library.model.Place
import javax.inject.Inject

class PlaceModelMapperImpl @Inject constructor() : ModelMapper<PlaceEntity, Place> {
    override fun fromEntity(from: PlaceEntity): Place {
        val place = Place(from.id, from.idPlace, from.name, from.bookmark)
        return place
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun toEntity(from: Place): PlaceEntity {
        val placeEntity =
            PlaceEntity(from.id, from.idPlace, from.name, from.bookmark)
        return placeEntity
    }
}