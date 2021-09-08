package com.app.mvvmtask.data.repository

import com.app.mvvmtask.data.api.ApiService
import com.app.mvvmtask.data.db.dao.UserDetailsDao
import com.app.mvvmtask.data.db.table.UserDetailsResponse
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.ui.base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService, private val userDao: UserDetailsDao,
) : BaseRepository() {

    var mPage = 1

    suspend fun getUser(): Flow<UserResponse> = flow {
        emit(apiService.getUser(mPage))
    }.flowOn(Dispatchers.IO)

    fun loadMore() {
        mPage += 1
    }

    suspend fun getUserDetails(userId: Int): Flow<UserDetailsResponse> = flow {
        emit(apiService.getUserDetails(userId))
    }.flowOn(Dispatchers.IO)


    /**Room Database*/

    suspend fun insertDetail(user: UserDetailsResponse.Data) {
        userDao.insert(user)
    }

    suspend fun isUserExist(id: Int): Boolean {
        return userDao.isUserExist(id)
    }

    suspend fun getUserDetailFromDB(id: Int): UserDetailsResponse.Data {
        return userDao.getUserDetailFromDB(id)
    }


}