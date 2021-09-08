package com.app.mvvmtask.data.db.dao

import androidx.room.*
import com.app.mvvmtask.data.db.table.UserDetailsResponse

@Dao
interface UserDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserDetailsResponse.Data): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: UserDetailsResponse.Data)

    @Query("SELECT EXISTS(SELECT * FROM UserDetail WHERE id=:id)")
    suspend fun isUserExist(id: Int): Boolean

    @Query("SELECT * FROM UserDetail WHERE id =:id")
    suspend fun getUserDetailFromDB(id: Int): UserDetailsResponse.Data

    @Query("DELETE FROM UserDetail")
    suspend fun deleteAll()

    @Query("DELETE FROM UserDetail WHERE id=:id")
    suspend fun deleteLead(id: Int)

}