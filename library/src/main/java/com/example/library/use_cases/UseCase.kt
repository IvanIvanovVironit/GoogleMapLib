//package com.example.library.use_cases
//
//import com.example.library.model.Place
//import com.example.library.repository.Repository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class UseCase @Inject constructor(private val repository: Repository) {
//    fun readAll(): Flow<List<Place>> {
//        return repository.readAll()
//    }
//
//    suspend fun insert (place: Place) {
//        repository.insert(place)
//    }
//
//    suspend fun update (place: Place) {
//        repository.update(place)
//    }
//
//    suspend fun delete(place: Place) {
//        repository.delete(place)
//    }
//
//    suspend fun insertAll(list: List<Place>) {
//        repository.insertAll(list)
//    }
//
//    suspend fun deleteAll() {
//        repository.deleteAll()
//    }
//}