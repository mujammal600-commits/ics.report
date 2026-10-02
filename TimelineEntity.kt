package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timeline_events")
data class TimelineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val caseId: String,
    val dateLabel: String,
    val eventTitle: String,
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
