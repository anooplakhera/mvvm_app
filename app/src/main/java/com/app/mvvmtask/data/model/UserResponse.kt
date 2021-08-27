package com.app.mvvmtask.data.model

import java.io.Serializable

data class UserResponse(
    val `data`: ArrayList<Data>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int,
) : Serializable {
    data class Data(
        val avatar: String,
        val email: String,
        var first_name: String,
        val id: Int,
        val last_name: String,
    ) : Serializable

    data class Support(
        val text: String,
        val url: String,
    ) : Serializable
}