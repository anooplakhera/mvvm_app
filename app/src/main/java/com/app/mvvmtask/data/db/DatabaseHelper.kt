package com.app.mvvmtask.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.mvvmtask.data.model.MovieResponse

@Database(entities = arrayOf(MovieResponse.SearchObj::class), version = 1, exportSchema = false)
abstract class DatabaseHelper : RoomDatabase() {

//    abstract fun movieListDao(): DaoAccess

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getDatabaseClient(context: Context): DatabaseHelper {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, DatabaseHelper::class.java, "MvvmTask")
                    .fallbackToDestructiveMigration()
                    .build()
                return INSTANCE!!
            }
        }
    }


}