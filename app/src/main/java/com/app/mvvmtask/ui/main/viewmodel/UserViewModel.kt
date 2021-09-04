package com.app.mvvmtask.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.data.repository.UserRepository
import com.app.mvvmtask.ui.base.BaseViewModel
import com.app.mvvmtask.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(private val networkCallRepo: UserRepository) : BaseViewModel() {

    var isLoading: Boolean = false

    private val _userStateFlow: MutableStateFlow<Resource<UserResponse>> =
        MutableStateFlow(Resource.loading(null))
    val userStateFlow: StateFlow<Resource<UserResponse>> = _userStateFlow

    fun getUser() = viewModelScope.launch {
        networkCallRepo.getUser().onStart {
            _userStateFlow.value = Resource.loading(null)
        }.catch { e ->
            _userStateFlow.value = Resource.error(e, null)
        }.collect {
            if (it.data.size > 0) isLoading = true
            _userStateFlow.value = Resource.success(it)
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
                                    delay(200)
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