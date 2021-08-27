package com.app.mvvmtask.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.data.api.ApiInterface
import com.app.mvvmtask.data.api.Resource
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.ui.base.BaseRepository
import com.app.mvvmtask.utils.NetworkHandling
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserRepository(val context: Context, val apiInterface: ApiInterface) : BaseRepository() {

    var mPage = 1
    var isLoading: Boolean = false
    val userLiveData = MutableLiveData<Resource<UserResponse>>()

    @SuppressLint("CheckResult")
    fun getUserList() {
        if (NetworkHandling.isNetworkConnected(context)) {
            userLiveData.postValue(Resource.loading(null))
            apiInterface!!.getUserList(mPage)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .doOnError {
                    userLiveData.postValue(Resource.error(it.message.toString(), null))
                    NetworkHandling.showNetworkError(context, it)
                }.subscribe({
                    try {
                        if (it.data?.size > 0) isLoading = true
                        userLiveData.postValue(Resource.success(it))
                    } catch (e: Exception) {
                        println(e.printStackTrace())
                    }
                }, { error ->
                    println("ThrowableError ${error.toString()}")
                })
        }
    }

    fun recycleLoadMore(rvList: RecyclerView) {
        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                rvList.getChildAt(rvList.childCount - 1).let {
                    (it.bottom - (rvList.height + rvList.scrollY)).let {
                        if (it == 0) {
                            if (isLoading) {
                                isLoading = false
                                Handler(Looper.getMainLooper()).postDelayed({
                                    loadMore()
                                }, 500)
                            }
                        }
                    }
                }
            }
        })
    }

    fun loadMore() {
        mPage += 1
        getUserList()
    }

}