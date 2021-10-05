//package com.example.library.database
//
//import androidx.room.*
//import kotlinx.coroutines.flow.Flow
//
//
//@Dao
//interface PlaceDao {
//    @Query("SELECT * FROM place_table ORDER BY id ASC")
//    fun readAllData(): Flow<List< PlaceEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insert(item:  PlaceEntity)
//
//    @Update(onConflict = OnConflictStrategy.IGNORE)
//    fun update(item:  PlaceEntity)
//
//    @Delete
//    fun delete(item:  PlaceEntity)
//
//    @Insert
//    fun insertAll(items: List< PlaceEntity>)
//
//    @Query("DELETE FROM place_table")
//    suspend fun deleteAll()
//}