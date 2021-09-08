package com.app.mvvmtask.ui.main.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.app.mvvmtask.R
import com.app.mvvmtask.databinding.ActivityUserDetailsBinding
import com.app.mvvmtask.ui.base.BaseActivity
import com.app.mvvmtask.ui.main.viewmodel.UserViewModel
import com.app.mvvmtask.utils.NetworkHandling
import com.app.mvvmtask.utils.SealedClasses.Status
import com.app.mvvmtask.utils.visible
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsActivity : BaseActivity<ActivityUserDetailsBinding>() {

    val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        intent?.let {
            it.getIntExtra("userId", 0).let { id ->
                GlobalScope.launch {
                    viewModel.isUserExist(id)
                    delay(200)
                    getUserDetails(id)
                }
            }
        }
        observeUserDetails()

        lifecycleScope.launchWhenStarted {
            viewModel._userDetail.collect {
                Glide.with(this@UserDetailsActivity).load(it!!.avatar).circleCrop()
                    .into(binding.imgUserImage)
            }
        }

    }

    private fun getUserDetails(id: Int) {
        if (viewModel.isUserDetailExits)
            viewModel.getUserDetailFromDB(id)
        else if (NetworkHandling.isNetworkConnected(this)) viewModel.getUserDetails(id)
    }

    private fun observeUserDetails() {
        lifecycleScope.launchWhenStarted {
            viewModel.userDetailsStateFlow.collect {
                when (it.status) {
                    Status.LOADING -> binding.progressIndicator.visible(true)
                    Status.SUCCESS -> binding.progressIndicator.visible(false)
                    Status.ERROR -> {
                        binding.progressIndicator.visible(false)
                        NetworkHandling.showNetworkError(this@UserDetailsActivity, it.throwable!!)
                    }
                    Status.EMPTY -> Unit
                }
            }
        }
    }

    override fun getActivityBinding(): ActivityUserDetailsBinding =
        DataBindingUtil.setContentView(this@UserDetailsActivity, R.layout.activity_user_details)!!
}