package com.app.mvvmtask.ui.main.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.data.repository.UserRepository
import com.app.mvvmtask.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class UserViewModel(private val networkCallRepo: UserRepository) : BaseViewModel() {

    var userLiveData = networkCallRepo.userLiveData

    @SuppressLint("CheckResult")
    fun getUserList() = viewModelScope.launch {
        networkCallRepo!!.getUserList()
    }

    @SuppressLint("CheckResult")
    fun recycleLoadMore(rvList: RecyclerView) = viewModelScope.launch {
        networkCallRepo!!.recycleLoadMore(rvList)
    }

}