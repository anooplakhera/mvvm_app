package com.app.mvvmtask.ui.main.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.data.api.Resource
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.data.repository.UserRepository
import com.app.mvvmtask.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(private val networkCallRepo: UserRepository) : BaseViewModel() {

    var isLoading: Boolean = false

    private val _apiStateFlow: MutableStateFlow<Resource<UserResponse>> =
        MutableStateFlow(Resource.loading(null))
    val apiStateFlow: StateFlow<Resource<UserResponse>> = _apiStateFlow

    @SuppressLint("CheckResult")
    fun getUser() = viewModelScope.launch {
        networkCallRepo.getUser().onStart {
            _apiStateFlow.value = Resource.loading(null)
        }.catch { e ->
            _apiStateFlow.value = Resource.error(e, null)
        }.collect {
            if (it.data.size > 0) isLoading = true
            _apiStateFlow.value = Resource.success(it)
        }
    }

    fun recycleLoadMore(rvList: RecyclerView) {
        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                rvList.getChildAt(rvList.childCount - 1).let {
                    (it.bottom - (rvList.height + rvList.scrollY)).let {
                        if (it == 0) {
                            viewModelScope.launch {
                                if (isLoading) {
                                    isLoading = false
                                    delay(500)
                                    networkCallRepo.loadMore()
                                    getUser()
                                }
                            }
                        }
                    }
                }
            }
        })
    }


}