package it.lam.pptproject.data.room

import it.lam.pptproject.utils.Utils
import java.util.Date

data class TrackingDataWithDate(
    val id: Int,
    val type: Utils.RecordType,
    val startTime: Date,
    val endTime: Date,
    val steps: Int,
)


