package com.app.mvvmtask.utils

import android.content.Context
import android.net.ConnectivityManager
import com.app.mvvmtask.R
import com.app.mvvmtask.ui.base.BaseActivity
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException

object NetworkHandling {

    fun showNetworkError(context: Context, exception: Throwable) {
        if (exception is HttpException) {
            val error: HttpException = exception
            when (exception.code()) {
                503 -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        context.apply { showToast(this, getString(R.string.server_error)) }
                    }
                }
                else -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        context.apply { showToast(this, getString(R.string.something_went_wrong)) }
                    }
                }
            }
        } else if (exception is SocketTimeoutException || exception is IOException) {
            CoroutineScope(Dispatchers.Main).launch {
                context.apply { showToast(this, getString(R.string.internet_is_slow)) }
            }
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        if (isConnected(context))
            return true
        else {
            CoroutineScope(Dispatchers.Main).launch {
                context.apply { showToast(this, getString(R.string.internet_not_connect)) }
            }
        }
        return false
    }

    private fun isConnected(context: Context): Boolean {
        val cm =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

}