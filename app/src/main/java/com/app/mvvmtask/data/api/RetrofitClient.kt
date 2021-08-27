package com.app.mvvmtask.data.api

import com.app.mvvmtask.BuildConfig
import com.app.mvvmtask.utils.NetworkLogger
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    fun <Api> getAPI(api: Class<Api>): Api {
        return Retrofit.Builder()
            .baseUrl(ApiUrls.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient).build()
            .create(api)
    }

    var gson = GsonBuilder().setLenient().create()

    private var okHttpClient =
        OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(
            60, TimeUnit.SECONDS
        ).writeTimeout(
            60, TimeUnit.SECONDS
        ).addInterceptor(NetworkLogger()).also { client ->
            if (BuildConfig.BUILD_TYPE.contentEquals("debug")) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                client.addInterceptor(logging)
            }
        }.build()

}