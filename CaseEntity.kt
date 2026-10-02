package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cases")
data class CaseEntity(
    @PrimaryKey
    val caseId: String,
    val targetUrl: String,
    val platform: String,
    val category: String,
    val priority: String,
    val status: String,
    val incidentDate: String,
    val description: String,
    val notes: String = "",
    val followUpDate: String = "",
    val createdTimestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis()
)
