package com.app.mvvmtask.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.mvvmtask.data.db.dao.UserDetailsDao
import com.app.mvvmtask.data.db.table.UserDetailsResponse
import com.app.mvvmtask.data.model.MovieResponse

@Database(
    entities = [MovieResponse.SearchObj::class, UserDetailsResponse.Data::class],
    version = 3, exportSchema = false
)
abstract class DatabaseHelper : RoomDatabase() {

    abstract fun userDetailsDao(): UserDetailsDao

}