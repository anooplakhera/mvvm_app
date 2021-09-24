package com.app.mvvmtask

import android.content.Context
import androidx.room.Room
import com.app.mvvmtask.data.db.DatabaseHelper
import com.app.mvvmtask.ui.main.adapter.UserAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getDataBase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, DatabaseHelper::class.java, "MvvmTask").build()

    @Singleton
    @Provides
    fun getUserDetailDao(db: DatabaseHelper) = db.userDetailsDao()

    @Singleton
    @Provides
    fun getUserAdapter(): UserAdapter = UserAdapter()

}
