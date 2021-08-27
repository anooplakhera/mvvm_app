package com.app.mvvmtask.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.app.mvvmtask.data.api.RetrofitClient

abstract class BaseActivity<VM : BaseViewModel, B : ViewBinding, RT : BaseRepository> :
    AppCompatActivity() {

    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val apiClient = RetrofitClient()

    abstract fun getViewModel(): Class<VM>
    abstract fun getActivityBinding(): B
    abstract fun getActivityRepository(): RT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getActivityBinding()

        ViewModelFactory(getActivityRepository()).let {
            viewModel = ViewModelProvider(this, it).get(getViewModel())
        }
    }

}