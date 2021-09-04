package com.app.mvvmtask.utils

import com.app.mvvmtask.utils.SealedClasses.Status

class Resource<out T>(val status: Status, val data: T?, val throwable: Throwable?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: Throwable, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }

}
