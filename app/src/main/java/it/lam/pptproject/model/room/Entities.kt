package it.lam.pptproject.model.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import it.lam.pptproject.utils.Tracker

@Entity
data class User(

    @PrimaryKey(autoGenerate = false)
    val username: String,

    var active : Boolean?
)

@Entity(foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["username"], childColumns = ["username"])])
data class TrackingData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: Tracker.RecordType,
    val startTime: Long,
    val endTime: Long,
    val values: String,
    val steps: Int?,
    val username: String
)
