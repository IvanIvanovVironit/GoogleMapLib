//package com.example.library.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(
//    entities = [
//        PlaceEntity::class
//               ], version = 1, exportSchema = false)
//abstract class MapDatabase: RoomDatabase() {
//
//    abstract fun placeDao(): PlaceDao
//
//    companion object {
//        @Volatile
//        var INSTANCE: MapDatabase? = null
//
//        fun getDatabase(context: Context): MapDatabase {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context,
//                    MapDatabase::class.java,
//                    "map_database"
//                ).build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
//}