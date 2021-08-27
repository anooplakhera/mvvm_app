package com.app.mvvmtask.ui.main.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.R
import com.app.mvvmtask.data.api.ApiInterface
import com.app.mvvmtask.data.api.Status
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.data.repository.UserRepository
import com.app.mvvmtask.databinding.ActivityMainBinding
import com.app.mvvmtask.ui.base.BaseActivity
import com.app.mvvmtask.ui.main.adapter.UserAdapter
import com.app.mvvmtask.ui.main.viewmodel.UserViewModel
import com.app.mvvmtask.utils.snackbar
import com.app.mvvmtask.utils.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<UserViewModel, ActivityMainBinding, UserRepository>() {

    var mAdapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Main).launch { bindList(binding.rvUserList) }

        getUserList()
        viewModel.recycleLoadMore(binding.rvUserList)
        observeUser()
    }

    private fun getUserList() {
        viewModel.getUserList()
    }

    private fun observeUser() {
        viewModel.userLiveData?.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> binding.progressIndicator.visible(true)
                Status.SUCCESS -> {
                    binding.progressIndicator.visible(false)
                    notifyList(it.data!!.data)
                }
                Status.ERROR -> binding.progressIndicator.visible(false)
            }
        })
    }

    private fun notifyList(data: ArrayList<UserResponse.Data>?) {
        data?.let {
            if (it.size > 0) mAdapter!!.addList(it)
            else binding.mainLayout.snackbar("No Data Found")
        } ?: binding.mainLayout.snackbar("No Data Found")
    }

    private fun bindList(rvList: RecyclerView) {
        val mList = ArrayList<UserResponse.Data>()
        rvList.apply {
            mAdapter = UserAdapter(mList!!) {}
            adapter = mAdapter
        }
    }

    override fun getViewModel(): Class<UserViewModel> = UserViewModel::class.java

    override fun getActivityBinding(): ActivityMainBinding =
        DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

    override fun getActivityRepository(): UserRepository =
        UserRepository(this, apiClient.getAPI(ApiInterface::class.java))

}