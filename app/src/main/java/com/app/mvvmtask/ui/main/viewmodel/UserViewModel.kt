package com.app.mvvmtask.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.data.repository.UserRepository
import com.app.mvvmtask.ui.base.BaseViewModel
import com.app.mvvmtask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepo: UserRepository) : BaseViewModel() {

    var isLoading: Boolean = false

    private val _userStateFlow: MutableStateFlow<Resource<UserResponse>> =
        MutableStateFlow(Resource.empty(null))
    val userStateFlow: StateFlow<Resource<UserResponse>> = _userStateFlow

    fun getUser() = viewModelScope.launch {
        userRepo.getUser().onStart {
            _userStateFlow.value = Resource.loading(null)
        }.catch { e ->
            _userStateFlow.value = Resource.error(e, null)
        }.collect {
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
                                    userRepo.loadMore()
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