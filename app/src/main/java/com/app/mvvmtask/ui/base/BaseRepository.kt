package com.app.mvvmtask.ui.base

import com.app.mvvmtask.utils.ApiException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class BaseRepository {

    companion object {
        private const val MESSAGE_KEY = "message"
        private const val ERROR_KEY = "error"
    }

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("Error"))
                } catch (e: JSONException) {
                }
            }
            throw ApiException(message.toString())
        }
    }


}