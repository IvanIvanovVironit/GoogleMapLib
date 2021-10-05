//package com.example.library.interactor
//
//import com.example.library.model.Place
//import com.example.library.use_cases.UseCase
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class Interactor @Inject constructor(val useCase: UseCase) {
//    fun readAll(): Flow<List<Place>> {
//        return useCase.readAll()
//    }
//
//    suspend fun insert(place: Place) {
//        useCase.insert(place)
//    }
//
//    suspend fun update(place: Place) {
//        useCase.update(place)
//    }
//
//    suspend fun delete(place: Place) {
//        useCase.delete(place)
//    }
//
//    suspend fun insertAll(list: List<Place>) {
//        useCase.insertAll(list)
//    }
//
//    suspend fun deleteAll() {
//        useCase.deleteAll()
//    }
//}