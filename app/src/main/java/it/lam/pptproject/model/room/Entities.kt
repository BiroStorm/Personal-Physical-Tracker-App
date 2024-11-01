package it.lam.pptproject.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import it.lam.pptproject.utils.Utils

@Entity
data class User(

    @ColumnInfo(name = "username", index = true)
    @PrimaryKey(autoGenerate = false)
    var username: String,

    var active : Boolean?
)

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["username"], childColumns = ["username"])])
data class TrackingData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: Utils.RecordType,
    val startTime: Long,
    val endTime: Long,
    val values: String,
    val steps: Int = 0,
    val username: String
)
