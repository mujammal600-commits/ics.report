package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.analyzer.EvidenceAnalysisReport
import com.example.analyzer.SmartEvidenceAnalyzer
import com.example.data.BackupManager
import com.example.data.local.AppDatabase
import com.example.data.local.CaseEntity
import com.example.data.local.EvidenceEntity
import com.example.data.local.TimelineEntity
import com.example.data.repository.CaseRepository
import com.example.model.CasePriority
import com.example.model.CaseStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CaseRepository
    private val prefs = application.getSharedPreferences("ics_prefs", Context.MODE_PRIVATE)

    // Language state: true = Bengali (বাংলা), false = English
    private val _isBengali = MutableStateFlow(prefs.getBoolean("pref_lang_bn", true))
    val isBengali: StateFlow<Boolean> = _isBengali.asStateFlow()

    // App Security state
    private val _isLocked = MutableStateFlow(prefs.getString("pref_pin", "")?.isNotEmpty() == true)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private val _appPin = MutableStateFlow(prefs.getString("pref_pin", "") ?: "")
    val appPin: StateFlow<String> = _appPin.asStateFlow()

    // Navigation and screen management
    private val _currentScreen = MutableStateFlow("dashboard") // dashboard, new_report, cases, case_detail, evidence_vault, report_view, official_reporting, backup_restore, settings
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _selectedCaseId = MutableStateFlow<String?>(null)
    val selectedCaseId: StateFlow<String?> = _selectedCaseId.asStateFlow()

    // Filters for Case List
    val searchQuery = MutableStateFlow("")
    val filterStatus = MutableStateFlow<String?>(null)
    val filterPriority = MutableStateFlow<String?>(null)
    val filterCategory = MutableStateFlow<String?>(null)

    // Raw database cases
    val allCases: StateFlow<List<CaseEntity>>
    val allEvidence: StateFlow<List<EvidenceEntity>>

    // Filtered cases
    val filteredCases: StateFlow<List<CaseEntity>>

    // Current selected case details
    private val _activeCase = MutableStateFlow<CaseEntity?>(null)
    val activeCase: StateFlow<CaseEntity?> = _activeCase.asStateFlow()

    private val _activeCaseEvidence = MutableStateFlow<List<EvidenceEntity>>(emptyList())
    val activeCaseEvidence: StateFlow<List<EvidenceEntity>> = _activeCaseEvidence.asStateFlow()

    private val _activeCaseTimeline = MutableStateFlow<List<TimelineEntity>>(emptyList())
    val activeCaseTimeline: StateFlow<List<TimelineEntity>> = _activeCaseTimeline.asStateFlow()

    // Live analyzer result for active creation/case
    private val _liveAnalysis = MutableStateFlow<EvidenceAnalysisReport?>(null)
    val liveAnalysis: StateFlow<EvidenceAnalysisReport?> = _liveAnalysis.asStateFlow()

    init {
        val db = AppDatabase.getInstance(application)
        repository = CaseRepository(db.caseDao())

        allCases = repository.allCases.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        allEvidence = repository.allEvidence.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        filteredCases = combine(
            allCases,
            searchQuery,
            filterStatus,
            filterPriority,
            filterCategory
        ) { cases, query, status, priority, category ->
            cases.filter { item ->
                val matchesQuery = query.isBlank() ||
                        item.caseId.contains(query, ignoreCase = true) ||
                        item.targetUrl.contains(query, ignoreCase = true) ||
                        item.description.contains(query, ignoreCase = true) ||
                        item.category.contains(query, ignoreCase = true)

                val matchesStatus = status == null || item.status.equals(status, ignoreCase = true)
                val matchesPriority = priority == null || item.priority.equals(priority, ignoreCase = true)
                val matchesCategory = category == null || item.category.equals(category, ignoreCase = true)

                matchesQuery && matchesStatus && matchesPriority && matchesCategory
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Seed initial sample cases if database is brand new so user immediately sees rich dashboard
        viewModelScope.launch {
            allCases.collect { list ->
                if (list.isEmpty() && !prefs.getBoolean("db_seeded", false)) {
                    seedInitialCases()
                    prefs.edit().putBoolean("db_seeded", true).apply()
                }
            }
        }
    }

    private suspend fun seedInitialCases() {
        val now = System.currentTimeMillis()
        val day = 86400000L
        val dateFmt = SimpleDateFormat("dd MMM yyyy", Locale.US)

        val case1 = CaseEntity(
            caseId = "ICS-2026-00001",
            targetUrl = "https://facebook.com/fake.identity.official.claim",
            platform = "Facebook",
            category = "impersonation",
            priority = "High",
            status = "Ready to Report",
            incidentDate = dateFmt.format(Date(now - 2 * day)),
            description = "A fake profile impersonating an executive, stealing profile pictures and bio, and requesting financial transfers via messenger.",
            notes = "Checked UID: 1000849201948; victim informed and submitted official identity proof.",
            followUpDate = dateFmt.format(Date(now + 3 * day)),
            createdTimestamp = now - 2 * day,
            updatedTimestamp = now - day
        )

        val case2 = CaseEntity(
            caseId = "ICS-2026-00002",
            targetUrl = "https://t.me/crypto_doubler_scam_bd",
            platform = "Telegram",
            category = "financial_scam",
            priority = "Critical",
            status = "Report Submitted",
            incidentDate = dateFmt.format(Date(now - 4 * day)),
            description = "Unauthorized financial scam group luring university students into fake investment tasks and collecting bKash deposits.",
            notes = "bKash wallet: 01700-000000, TrxID: 9XF84J2K. Escalated to CID Cyber Helpline.",
            followUpDate = dateFmt.format(Date(now + 1 * day)),
            createdTimestamp = now - 4 * day,
            updatedTimestamp = now - 2 * day
        )

        val case3 = CaseEntity(
            caseId = "ICS-2026-00003",
            targetUrl = "https://instagram.com/p/DF99281x",
            platform = "Instagram",
            category = "harassment",
            priority = "Medium",
            status = "Evidence Collecting",
            incidentDate = dateFmt.format(Date(now - 1 * day)),
            description = "Target account posting defamatory comments and coordinated abusive tagging.",
            notes = "Gathering second batch of screenshots from post comments.",
            followUpDate = dateFmt.format(Date(now + 5 * day)),
            createdTimestamp = now - day,
            updatedTimestamp = now
        )

        repository.insertCase(case1)
        repository.insertCase(case2)
        repository.insertCase(case3)

        // Seed evidence
        repository.addEvidence(
            EvidenceEntity(
                caseId = "ICS-2026-00001",
                type = "Screenshot",
                title = "Fake Profile Bio & Avatar Comparison",
                contentOrUri = "screenshot_profile_impersonation.jpg",
                description = "Side by side comparison of authentic profile and fraudulent copy."
            )
        )
        repository.addEvidence(
            EvidenceEntity(
                caseId = "ICS-2026-00001",
                type = "Chat Log",
                title = "Messenger Money Demand",
                contentOrUri = "chat_log_request.txt",
                description = "Fraudulent request demanding emergency emergency loan."
            )
        )
        repository.addEvidence(
            EvidenceEntity(
                caseId = "ICS-2026-00002",
                type = "Payment Receipt",
                title = "bKash Transfer Statement TrxID: 9XF84J2K",
                contentOrUri = "bkash_statement.pdf",
                description = "Digital deposit proof with timestamp and recipient wallet."
            )
        )

        // Seed timeline
        repository.addTimelineEvent(
            TimelineEntity(
                caseId = "ICS-2026-00001",
                dateLabel = dateFmt.format(Date(now - 2 * day)),
                eventTitle = "Fraudulent account discovered",
                description = "Colleagues noticed friend request from duplicated name."
            )
        )
        repository.addTimelineEvent(
            TimelineEntity(
                caseId = "ICS-2026-00001",
                dateLabel = dateFmt.format(Date(now - day)),
                eventTitle = "Screenshots preserved & Report drafted",
                description = "Full unedited chat logs archived in ICS Pro."
            )
        )
    }

    fun setLanguage(bengali: Boolean) {
        _isBengali.value = bengali
        prefs.edit().putBoolean("pref_lang_bn", bengali).apply()
    }

    fun setPin(pin: String) {
        _appPin.value = pin
        prefs.edit().putString("pref_pin", pin).apply()
        _isLocked.value = false
    }

    fun removePin() {
        _appPin.value = ""
        prefs.edit().remove("pref_pin").apply()
        _isLocked.value = false
    }

    fun unlockApp(pin: String): Boolean {
        if (pin == _appPin.value) {
            _isLocked.value = false
            return true
        }
        return false
    }

    fun lockApp() {
        if (_appPin.value.isNotEmpty()) {
            _isLocked.value = true
        }
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun selectCase(caseId: String) {
        _selectedCaseId.value = caseId
        viewModelScope.launch {
            repository.getCaseById(caseId).collect { c ->
                _activeCase.value = c
                if (c != null) {
                    runLiveAnalysis(c.targetUrl, c.description, c.platform, c.category, c.incidentDate)
                }
            }
        }
        viewModelScope.launch {
            repository.getEvidenceForCase(caseId).collect { evList ->
                _activeCaseEvidence.value = evList
            }
        }
        viewModelScope.launch {
            repository.getTimelineForCase(caseId).collect { tList ->
                _activeCaseTimeline.value = tList
            }
        }
        _currentScreen.value = "case_detail"
    }

    fun runLiveAnalysis(
        targetUrl: String,
        description: String,
        platform: String,
        category: String,
        incidentDate: String,
        evidenceCount: Int = _activeCaseEvidence.value.size,
        hasScreenshots: Boolean = _activeCaseEvidence.value.any { it.type == "Screenshot" }
    ) {
        _liveAnalysis.value = SmartEvidenceAnalyzer.analyze(
            targetUrl = targetUrl,
            description = description,
            platform = platform,
            currentCategory = category,
            evidenceCount = evidenceCount,
            hasScreenshots = hasScreenshots,
            incidentDate = incidentDate
        )
    }

    fun createCase(
        targetUrl: String,
        platform: String,
        category: String,
        priority: String,
        incidentDate: String,
        description: String,
        notes: String = "",
        followUpDate: String = "",
        evidenceTitles: List<Pair<String, String>> = emptyList() // title, type/uri
    ) {
        viewModelScope.launch {
            val count = allCases.value.size
            val newId = repository.generateNewCaseId(count)
            val newCase = CaseEntity(
                caseId = newId,
                targetUrl = targetUrl.trim(),
                platform = platform,
                category = category,
                priority = priority,
                status = CaseStatus.DRAFT.key,
                incidentDate = incidentDate,
                description = description.trim(),
                notes = notes,
                followUpDate = followUpDate
            )
            repository.insertCase(newCase)

            // Add initial timeline event
            val today = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date())
            repository.addTimelineEvent(
                TimelineEntity(
                    caseId = newId,
                    dateLabel = today,
                    eventTitle = "Case created in ICS Pro",
                    description = "Initial incident documentation logged."
                )
            )

            // Add attached evidence items if any
            evidenceTitles.forEach { (title, uri) ->
                repository.addEvidence(
                    EvidenceEntity(
                        caseId = newId,
                        type = "Screenshot",
                        title = title,
                        contentOrUri = uri,
                        description = "Evidence captured during case filing."
                    )
                )
            }

            selectCase(newId)
        }
    }

    fun updateCaseStatus(status: String) {
        val current = _activeCase.value ?: return
        viewModelScope.launch {
            val updated = current.copy(status = status)
            repository.updateCase(updated)
            _activeCase.value = updated

            val today = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date())
            repository.addTimelineEvent(
                TimelineEntity(
                    caseId = current.caseId,
                    dateLabel = today,
                    eventTitle = "Status updated to $status",
                    description = "Case progression state recorded."
                )
            )
        }
    }

    fun updateCasePriority(priority: String) {
        val current = _activeCase.value ?: return
        viewModelScope.launch {
            val updated = current.copy(priority = priority)
            repository.updateCase(updated)
            _activeCase.value = updated
        }
    }

    fun updateCaseNotes(notes: String) {
        val current = _activeCase.value ?: return
        viewModelScope.launch {
            val updated = current.copy(notes = notes)
            repository.updateCase(updated)
            _activeCase.value = updated
        }
    }

    fun setFollowUpDate(dateStr: String) {
        val current = _activeCase.value ?: return
        viewModelScope.launch {
            val updated = current.copy(followUpDate = dateStr)
            repository.updateCase(updated)
            _activeCase.value = updated
        }
    }

    fun addEvidenceItem(caseId: String, title: String, type: String, contentOrUri: String, description: String = "") {
        viewModelScope.launch {
            repository.addEvidence(
                EvidenceEntity(
                    caseId = caseId,
                    type = type,
                    title = title,
                    contentOrUri = contentOrUri,
                    description = description
                )
            )
            val today = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date())
            repository.addTimelineEvent(
                TimelineEntity(
                    caseId = caseId,
                    dateLabel = today,
                    eventTitle = "Evidence added: $title",
                    description = "Type: $type"
                )
            )
        }
    }

    fun deleteEvidenceItem(id: Long) {
        viewModelScope.launch {
            repository.deleteEvidence(id)
        }
    }

    fun addTimelineMilestone(caseId: String, dateLabel: String, title: String, desc: String) {
        viewModelScope.launch {
            repository.addTimelineEvent(
                TimelineEntity(
                    caseId = caseId,
                    dateLabel = dateLabel,
                    eventTitle = title,
                    description = desc
                )
            )
        }
    }

    fun deleteCase(caseId: String) {
        viewModelScope.launch {
            repository.deleteCase(caseId)
            _currentScreen.value = "cases"
            _selectedCaseId.value = null
            _activeCase.value = null
        }
    }

    fun wipeAllData() {
        viewModelScope.launch {
            repository.clearAllData()
            _activeCase.value = null
            _selectedCaseId.value = null
            _currentScreen.value = "dashboard"
        }
    }

    fun exportBackupJson(): String {
        return BackupManager.exportToJson(
            cases = allCases.value,
            evidence = allEvidence.value,
            timeline = emptyList() // or can retrieve all
        )
    }

    fun importBackupJson(jsonString: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val payload = BackupManager.parseFromJson(jsonString)
                repository.importData(payload.cases, payload.evidence, payload.timeline)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to parse backup JSON")
            }
        }
    }
}
