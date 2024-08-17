package it.lam.pptproject.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val username: String,

    var active : Boolean?
)

@Entity
data class Activity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dataType: String,
    val startTime: Long,
    val endTime: Long,
    val values: String,
    val username: String
)
