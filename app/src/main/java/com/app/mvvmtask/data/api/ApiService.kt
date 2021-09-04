package com.app.mvvmtask.data.api

import android.util.Log
import com.app.mvvmtask.data.model.UserResponse
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*

class ApiService {

    private val client = HttpClient(Android) {
        install(DefaultRequest) {
            headers.append("Content-Type", "application/json")
        }

        defaultRequest {
            url.takeFrom(URLBuilder().takeFrom(ApiUrls.BASE_URL).apply {
                encodedPath += url.encodedPath
            })
        }

        install(JsonFeature) { serializer = GsonSerializer() }

        // Logging
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Ktor", message)
                }
            }
            level = LogLevel.ALL
        }
        //Timeout
        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }

        /*HttpResponseValidator{
            validateResponse { response: HttpResponse ->
                val statusCode = response.status.value

                println("HTTP status: $statusCode")

                when (statusCode) {
                    in 300..399 -> throw RedirectResponseException(response)
                    in 400..499 -> throw ClientRequestException(response)
                    in 500..599 -> throw ServerResponseException(response)
                }

                if (statusCode >= 600) {
                    throw ResponseException(response)
                }
            }

            handleResponseException { cause: Throwable ->
                throw cause
            }
        }*/
    }

    // API Request
    suspend fun getUser(page: Int): UserResponse {
        return client.get(ApiUrls.USER_API) {
            parameter("page", page)
        }
    }
}