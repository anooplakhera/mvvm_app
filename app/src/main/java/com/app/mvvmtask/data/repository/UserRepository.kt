package com.app.mvvmtask.data.repository

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.mvvmtask.data.api.ApiInterface
import com.app.mvvmtask.data.api.Resource
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.ui.base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(val context: Context, val apiInterface: ApiInterface) : BaseRepository() {

    var mPage = 1

    @SuppressLint("CheckResult")
    suspend fun getUserList(): Flow<UserResponse> {
        return flow {
            emit(apiInterface!!.getUserList(mPage))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun loadMore() {
        mPage += 1
        getUserList()
    }

}