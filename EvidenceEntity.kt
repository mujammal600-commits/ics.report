package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evidence_items")
data class EvidenceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val caseId: String,
    val type: String, // Screenshot, URL, Note, Document, Chat Log, Payment Receipt
    val title: String,
    val contentOrUri: String,
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
