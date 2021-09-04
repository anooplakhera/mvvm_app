package com.app.mvvmtask.data.repository

import com.app.mvvmtask.data.api.ApiService
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.ui.base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(val apiService: ApiService) : BaseRepository() {

    var mPage = 1

    suspend fun getUser(): Flow<UserResponse> = flow {
        emit(apiService.getUser(mPage))
    }.flowOn(Dispatchers.IO)

    fun loadMore() {
        mPage += 1
    }

}