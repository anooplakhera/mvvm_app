package com.app.mvvmtask.data.api

import com.app.mvvmtask.data.model.UserResponse
import io.ktor.client.request.*
import javax.inject.Inject


class ApiService @Inject constructor() {

    suspend fun getUser(page: Int): UserResponse {
        return client.get(ApiUrls.USER_API) {
            parameter("page", page)
        }
    }

}