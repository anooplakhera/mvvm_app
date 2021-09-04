package com.app.mvvmtask.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.databinding.ItemUserRawBinding
import com.bumptech.glide.Glide

class UserAdapter(val mList: ArrayList<UserResponse.Data>, var itemClick: (Int) -> Unit) :
    RecyclerView.Adapter<UserAdapter.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserRawBinding.inflate(inflater)
        return MyHolder(binding)
    }

    fun addList(mObj: ArrayList<UserResponse.Data>) {
        val size = this.mList.size
        this.mList.addAll(mObj)
        val sizeNew = this.mList.size
        notifyItemRangeChanged(size, sizeNew)
    }
    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: MyHolder, position: Int) = holder.bind(mList[position])

    inner class MyHolder(val binding: ItemUserRawBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserResponse.Data) {
            binding.user = user
            binding.executePendingBindings()
            Glide.with(itemView.context).load(user.avatar).circleCrop().into(binding.imgUserImage)
            binding.rytMain.setOnClickListener { itemClick(bindingAdapterPosition) }
        }
    }
}