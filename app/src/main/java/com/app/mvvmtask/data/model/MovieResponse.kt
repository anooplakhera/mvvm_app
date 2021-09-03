package com.app.mvvmtask.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class MovieResponse(
    val Response: String,
    val Search: List<SearchObj>,
    val totalResults: String,
    val Error: String,
) {
    @Entity(tableName = "MovieTable")
    data class SearchObj(
        @ColumnInfo(name = "poster")
        val Poster: String,
        @ColumnInfo(name = "title")
        val Title: String,
        @ColumnInfo(name = "type")
        val Type: String,
        @ColumnInfo(name = "year")
        val Year: String,
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "imdbID")
        val imdbID: String,
    )
}