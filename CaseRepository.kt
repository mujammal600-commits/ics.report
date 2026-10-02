package com.example.data.repository

import com.example.data.local.CaseDao
import com.example.data.local.CaseEntity
import com.example.data.local.EvidenceEntity
import com.example.data.local.TimelineEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CaseRepository(private val caseDao: CaseDao) {

    val allCases: Flow<List<CaseEntity>> = caseDao.getAllCases()
    val allEvidence: Flow<List<EvidenceEntity>> = caseDao.getAllEvidence()

    fun getCaseById(caseId: String): Flow<CaseEntity?> = caseDao.getCaseById(caseId)

    fun getEvidenceForCase(caseId: String): Flow<List<EvidenceEntity>> =
        caseDao.getEvidenceForCase(caseId)

    fun getTimelineForCase(caseId: String): Flow<List<TimelineEntity>> =
        caseDao.getTimelineForCase(caseId)

    suspend fun insertCase(caseEntity: CaseEntity) {
        caseDao.insertCase(caseEntity)
    }

    suspend fun updateCase(caseEntity: CaseEntity) {
        caseDao.updateCase(caseEntity.copy(updatedTimestamp = System.currentTimeMillis()))
    }

    suspend fun deleteCase(caseId: String) {
        caseDao.deleteEvidenceForCase(caseId)
        caseDao.deleteTimelineForCase(caseId)
        caseDao.deleteCaseById(caseId)
    }

    suspend fun addEvidence(evidence: EvidenceEntity): Long {
        return caseDao.insertEvidence(evidence)
    }

    suspend fun deleteEvidence(id: Long) {
        caseDao.deleteEvidenceById(id)
    }

    suspend fun addTimelineEvent(event: TimelineEntity): Long {
        return caseDao.insertTimelineEvent(event)
    }

    suspend fun clearAllData() {
        caseDao.deleteAllEvidence()
        caseDao.deleteAllTimeline()
        caseDao.deleteAllCases()
    }

    suspend fun importData(
        cases: List<CaseEntity>,
        evidence: List<EvidenceEntity>,
        timeline: List<TimelineEntity>
    ) {
        cases.forEach { caseDao.insertCase(it) }
        evidence.forEach { caseDao.insertEvidence(it) }
        timeline.forEach { caseDao.insertTimelineEvent(it) }
    }

    fun generateNewCaseId(existingCount: Int): String {
        val year = SimpleDateFormat("yyyy", Locale.US).format(Date())
        val number = existingCount + 1
        return "ICS-$year-${number.toString().padStart(5, '0')}"
    }
}
