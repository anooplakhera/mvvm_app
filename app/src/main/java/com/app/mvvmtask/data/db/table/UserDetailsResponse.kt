package com.app.mvvmtask.data.db.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

data class UserDetailsResponse(
    val `data`: Data,
    val support: Support
) : Serializable {
    @Entity(tableName = "UserDetail", indices = [Index(value = ["id"], unique = true)])
    data class Data(
        @ColumnInfo(name = "avatar")
        var avatar: String,
        @ColumnInfo(name = "email")
        var email: String,
        @ColumnInfo(name = "first_name")
        var first_name: String,
        @ColumnInfo(name = "id")
        var id: Int,
        @ColumnInfo(name = "last_name")
        var last_name: String
    ) : Serializable {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "rawId")
        var rawId: Int =0
    }

    data class Support(
        val text: String,
        val url: String
    ) : Serializable
}