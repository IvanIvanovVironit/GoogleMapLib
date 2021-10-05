//package com.example.library.repository
//
//import com.example.library.model.Place
//import kotlinx.coroutines.flow.Flow
//
//interface Repository {
//    fun readAll(): Flow<List<Place>>
//    suspend fun delete(place: Place)
//    suspend fun update(place: Place)
//    suspend fun insert(place: Place)
//    suspend fun deleteAll()
//    suspend fun insertAll(list: List<Place>)
//}