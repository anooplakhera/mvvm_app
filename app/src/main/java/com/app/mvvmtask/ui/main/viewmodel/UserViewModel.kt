package com.app.mvvmtask.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.data.db.table.UserDetailsResponse
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
    var isUserDetailExits: Boolean = false

    private val _userStateFlow: MutableStateFlow<Resource<UserResponse>> =
        MutableStateFlow(Resource.empty(null))
    val userStateFlow: StateFlow<Resource<UserResponse>> = _userStateFlow

    private val _userDetailStateFlow: MutableStateFlow<Resource<UserDetailsResponse>> =
        MutableStateFlow(Resource.empty(null))
    val userDetailsStateFlow: StateFlow<Resource<UserDetailsResponse>> = _userDetailStateFlow
    var _userDetail =
        MutableStateFlow<UserDetailsResponse.Data?>(UserDetailsResponse.Data("", "", "", 0, ""))

    fun getUser() = viewModelScope.launch {
        userRepo.getUser().onStart {
            _userStateFlow.value = Resource.loading(null)
        }.catch { e ->
            _userStateFlow.value = Resource.error(e, null)
        }.collect {
            _userStateFlow.value = Resource.success(it)
        }
    }

    fun getUserDetails(userId: Int) = viewModelScope.launch {
        userRepo.getUserDetails(userId).onStart {
            _userDetailStateFlow.value = Resource.loading(null)
        }.catch { e ->
            _userDetailStateFlow.value = Resource.error(e, null)
        }.collect {
            it.data.apply { _userDetail.value = this }
            _userDetailStateFlow.value = Resource.success(it)
            if (!isUserDetailExits) insertDetail(it.data)
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


    /**Room Database*/

    private fun insertDetail(user: UserDetailsResponse.Data) = viewModelScope.launch {
        userRepo.insertDetail(user)
    }

    fun isUserExist(id: Int) = viewModelScope.launch {
        isUserDetailExits = userRepo.isUserExist(id)
    }

    fun getUserDetailFromDB(id: Int) = viewModelScope.launch {
        userRepo.getUserDetailFromDB(id).let {
            it.apply {
                _userDetail.value = this
            }
        }
    }

}