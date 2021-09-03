package com.app.mvvmtask.data.api

import com.app.mvvmtask.data.model.MovieResponse
import com.app.mvvmtask.data.model.UserResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {


    @GET(ApiUrls.USER_API)
    suspend fun getUserList(@Query("page") page: Int): UserResponse

    @GET("?apikey=e5311742")
    suspend fun getMovieList(
        @Query("s") movieName: String,
        @Query("pages") pages: Int,
    ): Observable<MovieResponse>

}