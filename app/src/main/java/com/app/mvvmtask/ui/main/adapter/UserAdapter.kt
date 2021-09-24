package com.app.mvvmtask.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvmtask.data.model.UserResponse
import com.app.mvvmtask.databinding.ItemUserRawBinding
import com.bumptech.glide.Glide

class UserAdapter : ListAdapter<UserResponse.Data, UserAdapter.MyHolder>(UserItemDiffCallback()) {

    class UserItemDiffCallback : DiffUtil.ItemCallback<UserResponse.Data>() {
        override fun areItemsTheSame(oldItem: UserResponse.Data, newItem: UserResponse.Data):
                Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: UserResponse.Data, newItem: UserResponse.Data
        ): Boolean = oldItem == newItem
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserRawBinding.inflate(inflater)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) =
        holder.bind(getItem(position)!!)

    inner class MyHolder(private val binding: ItemUserRawBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userItem: UserResponse.Data) {
            with(binding) {
                user = userItem
                executePendingBindings()
                Glide.with(itemView.context).load(userItem.avatar).circleCrop().into(imgUserImage)
                root.setOnClickListener { onItemClickListener?.let { it(userItem) } }
            }
        }
    }

    private var onItemClickListener: ((UserResponse.Data) -> Unit)? = null
    fun setOnContainerClickListener(listener: (UserResponse.Data) -> Unit) {
        onItemClickListener = listener
    }
}