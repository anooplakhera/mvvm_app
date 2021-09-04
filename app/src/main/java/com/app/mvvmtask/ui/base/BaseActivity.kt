package com.app.mvvmtask.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.app.mvvmtask.data.api.ApiService

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: B
    protected val apiService = ApiService()

    abstract fun getActivityBinding(): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getActivityBinding()
    }

}