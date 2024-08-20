package it.lam.pptproject.utils

import android.util.Log

// An Singleton (object) where to save the data of the current tracking
// that will be saved later into the database as TrackerData
object Tracker {
    private var startTime: Long? = null
    private var endTime: Long? = null
    private var type: RecordType = RecordType.WALKING
    private var steps: Int = 0
    private var username: String = "unknown"


    fun start() {
        Log.d("Tracker", "Starting tracking")
        startTime = System.currentTimeMillis()
    }

    fun stop() {
        Log.d("Tracker", "Stopping tracking")
        endTime = System.currentTimeMillis()
    }

    fun clearData() {
        this.startTime = null
        this.endTime = null
        this.type = RecordType.WALKING
        this.steps = 0

    }

    // SETTERS

    fun setType(type: RecordType) {
        Log.d("Tracker", "Setting type to $type")
        this.type = type
    }

    fun setStartTime(startTime: Long) {
        Log.d("Tracker", "Setting start time to $startTime")
        this.startTime = startTime
    }

    fun setEndTime(endTime: Long) {
        Log.d("Tracker", "Setting end time to $endTime")
        this.endTime = endTime
    }

    fun setSteps(steps: Int) {
        Log.d("Tracker", "Setting steps to $steps")
        this.steps = steps
    }

    fun setUsername(username: String) {
        Log.d("Tracker", "Setting username to $username")
        this.username = username
    }

    // GETTERS

    fun isTracking(): Boolean {
        return startTime != null && endTime == null
    }

    fun getStartTime(): Long? {
        return startTime
    }

    fun getEndTime(): Long? {
        return endTime
    }

    fun getType(): RecordType {
        return type
    }

    fun getSteps(): Int {
        return steps
    }

    fun getUsername(): String {
        return username
    }

    override fun toString(): String {
        return "Tracker(startTime=$startTime, endTime=$endTime, type=$type, steps=$steps, username='$username')"
    }

    enum class RecordType {
         WALKING, DRIVING, SITTING
    }




}