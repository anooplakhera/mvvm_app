package com.app.mvvmtask.ui.main.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.R
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.databinding.ActivityMainBinding
import com.app.mvvmtask.ui.base.BaseActivity
import com.app.mvvmtask.ui.main.adapter.UserAdapter
import com.app.mvvmtask.ui.main.viewmodel.UserViewModel
import com.app.mvvmtask.utils.NetworkHandling
import com.app.mvvmtask.utils.SealedClasses.Status
import com.app.mvvmtask.utils.snackbar
import com.app.mvvmtask.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var mAdapter: UserAdapter
    var userList = ArrayList<UserResponse.Data>()
    val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch { bindList(binding.rvUserList) }
        userList = ArrayList()
        if (NetworkHandling.isNetworkConnected(this)) viewModel.getUser()
        viewModel.recycleLoadMore(binding.rvUserList)
        observeUser()
    }

    private fun observeUser() {
        lifecycleScope.launchWhenStarted {
            viewModel.userStateFlow.collect {
                when (it.status) {
                    Status.LOADING -> binding.progressIndicator.visible(true)
                    Status.SUCCESS -> {
                        binding.progressIndicator.visible(false)
                        notifyList(it.data!!.data)
                    }
                    Status.ERROR -> {
                        binding.progressIndicator.visible(false)
                        NetworkHandling.showNetworkError(this@MainActivity, it.throwable!!)
                    }
                    Status.EMPTY -> Unit
                }
            }
        }
    }

    private fun notifyList(data: ArrayList<UserResponse.Data>?) {
        binding.apply {
            data?.let {
                if (it.size > 0) {
                    viewModel.isLoading = true
                    userList.addAll(it)
                    mAdapter.submitList(userList.toMutableList())
                } else mainLayout.snackbar("No Data Found")
            } ?: mainLayout.snackbar("No Data Found")
        }
    }

    private fun bindList(rvList: RecyclerView) {
        rvList.adapter = mAdapter
        mAdapter.setOnContainerClickListener {
            startActivity(Intent(this@MainActivity, UserDetailsActivity::class.java).apply {
                putExtra("userId", it.id)
            })
        }
    }

    override fun getActivityBinding(): ActivityMainBinding =
        DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

}