package com.app.mvvmtask.data.api

import com.app.mvvmtask.data.model.UserResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET(ApiUrls.USER_API)
    fun getUserList(@Query("page") page: Int): Observable<UserResponse>

}