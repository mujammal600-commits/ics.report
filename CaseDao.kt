package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CaseDao {

    @Query("SELECT * FROM cases ORDER BY updatedTimestamp DESC")
    fun getAllCases(): Flow<List<CaseEntity>>

    @Query("SELECT * FROM cases WHERE caseId = :caseId LIMIT 1")
    fun getCaseById(caseId: String): Flow<CaseEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCase(caseEntity: CaseEntity)

    @Update
    suspend fun updateCase(caseEntity: CaseEntity)

    @Query("DELETE FROM cases WHERE caseId = :caseId")
    suspend fun deleteCaseById(caseId: String)

    @Query("DELETE FROM cases")
    suspend fun deleteAllCases()

    // Evidence
    @Query("SELECT * FROM evidence_items WHERE caseId = :caseId ORDER BY timestamp ASC")
    fun getEvidenceForCase(caseId: String): Flow<List<EvidenceEntity>>

    @Query("SELECT * FROM evidence_items ORDER BY timestamp DESC")
    fun getAllEvidence(): Flow<List<EvidenceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidence(evidence: EvidenceEntity): Long

    @Query("DELETE FROM evidence_items WHERE id = :id")
    suspend fun deleteEvidenceById(id: Long)

    @Query("DELETE FROM evidence_items WHERE caseId = :caseId")
    suspend fun deleteEvidenceForCase(caseId: String)

    @Query("DELETE FROM evidence_items")
    suspend fun deleteAllEvidence()

    // Timeline
    @Query("SELECT * FROM timeline_events WHERE caseId = :caseId ORDER BY timestamp ASC")
    fun getTimelineForCase(caseId: String): Flow<List<TimelineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineEvent(event: TimelineEntity): Long

    @Query("DELETE FROM timeline_events WHERE caseId = :caseId")
    suspend fun deleteTimelineForCase(caseId: String)

    @Query("DELETE FROM timeline_events")
    suspend fun deleteAllTimeline()
}
