package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.AppNavigationBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.SecurityLockScreen
import com.example.ui.screens.BackupRestoreScreen
import com.example.ui.screens.CaseDetailScreen
import com.example.ui.screens.CaseListScreen
import com.example.ui.screens.EvidenceVaultScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NewReportScreen
import com.example.ui.screens.OfficialReportingScreen
import com.example.ui.screens.ReportViewScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.CyberNavyDark
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: MainViewModel) {
    val isBengali by viewModel.isBengali.collectAsStateWithLifecycle()
    val isLocked by viewModel.isLocked.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val appPin by viewModel.appPin.collectAsStateWithLifecycle()

    val allCases by viewModel.allCases.collectAsStateWithLifecycle()
    val allEvidence by viewModel.allEvidence.collectAsStateWithLifecycle()
    val filteredCases by viewModel.filteredCases.collectAsStateWithLifecycle()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterStatus by viewModel.filterStatus.collectAsStateWithLifecycle()
    val filterPriority by viewModel.filterPriority.collectAsStateWithLifecycle()

    val activeCase by viewModel.activeCase.collectAsStateWithLifecycle()
    val activeEvidence by viewModel.activeCaseEvidence.collectAsStateWithLifecycle()
    val activeTimeline by viewModel.activeCaseTimeline.collectAsStateWithLifecycle()

    // Handle back button navigation
    BackHandler(enabled = currentScreen != "dashboard") {
        when (currentScreen) {
            "report_view" -> viewModel.navigateTo("case_detail")
            "case_detail" -> viewModel.navigateTo("cases")
            else -> viewModel.navigateTo("dashboard")
        }
    }

    // App Security Lock Screen
    if (isLocked && appPin.isNotEmpty()) {
        SecurityLockScreen(
            isBengali = isBengali,
            onUnlock = { pin -> viewModel.unlockApp(pin) }
        )
        return
    }

    Scaffold(
        containerColor = CyberNavyDark,
        topBar = {
            AppTopBar(
                currentScreen = currentScreen,
                isBengali = isBengali,
                onNavigateBack = {
                    when (currentScreen) {
                        "report_view" -> viewModel.navigateTo("case_detail")
                        "case_detail" -> viewModel.navigateTo("cases")
                        else -> viewModel.navigateTo("dashboard")
                    }
                },
                onToggleLanguage = { viewModel.setLanguage(!isBengali) },
                onOpenSettings = { viewModel.navigateTo("settings") },
                onLockApp = { viewModel.lockApp() },
                hasPin = appPin.isNotEmpty()
            )
        },
        bottomBar = {
            AppNavigationBar(
                currentScreen = currentScreen,
                isBengali = isBengali,
                onNavigate = { route -> viewModel.navigateTo(route) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                "dashboard" -> {
                    HomeScreen(
                        isBengali = isBengali,
                        cases = allCases,
                        onNavigate = { route -> viewModel.navigateTo(route) },
                        onSelectCase = { id -> viewModel.selectCase(id) }
                    )
                }

                "new_report" -> {
                    val nextId = "ICS-2026-${(allCases.size + 1).toString().padStart(5, '0')}"
                    NewReportScreen(
                        isBengali = isBengali,
                        nextCaseId = nextId,
                        onSaveCase = { url, platform, cat, prio, incDate, desc, notes, followUp, evList ->
                            viewModel.createCase(
                                targetUrl = url,
                                platform = platform,
                                category = cat,
                                priority = prio,
                                incidentDate = incDate,
                                description = desc,
                                notes = notes,
                                followUpDate = followUp,
                                evidenceTitles = evList
                            )
                        },
                        onCancel = { viewModel.navigateTo("dashboard") }
                    )
                }

                "cases" -> {
                    CaseListScreen(
                        isBengali = isBengali,
                        cases = filteredCases,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.searchQuery.value = it },
                        selectedStatus = filterStatus,
                        onStatusFilterChange = { viewModel.filterStatus.value = it },
                        selectedPriority = filterPriority,
                        onPriorityFilterChange = { viewModel.filterPriority.value = it },
                        onSelectCase = { id -> viewModel.selectCase(id) }
                    )
                }

                "case_detail" -> {
                    if (activeCase != null) {
                        CaseDetailScreen(
                            isBengali = isBengali,
                            case = activeCase!!,
                            evidenceList = activeEvidence,
                            timelineList = activeTimeline,
                            onUpdateStatus = { st -> viewModel.updateCaseStatus(st) },
                            onUpdatePriority = { p -> viewModel.updateCasePriority(p) },
                            onUpdateNotes = { n -> viewModel.updateCaseNotes(n) },
                            onSetFollowUpDate = { d -> viewModel.setFollowUpDate(d) },
                            onAddEvidence = { title, type, uri, desc ->
                                viewModel.addEvidenceItem(activeCase!!.caseId, title, type, uri, desc)
                            },
                            onDeleteEvidence = { id -> viewModel.deleteEvidenceItem(id) },
                            onAddTimelineMilestone = { dLabel, title, desc ->
                                viewModel.addTimelineMilestone(activeCase!!.caseId, dLabel, title, desc)
                            },
                            onGenerateReport = { viewModel.navigateTo("report_view") },
                            onOpenOfficialHelp = { viewModel.navigateTo("official_reporting") },
                            onDeleteCase = { viewModel.deleteCase(activeCase!!.caseId) }
                        )
                    } else {
                        viewModel.navigateTo("cases")
                    }
                }

                "report_view" -> {
                    if (activeCase != null) {
                        ReportViewScreen(
                            isBengali = isBengali,
                            case = activeCase!!,
                            evidenceList = activeEvidence,
                            timelineList = activeTimeline
                        )
                    } else {
                        viewModel.navigateTo("cases")
                    }
                }

                "evidence_vault" -> {
                    EvidenceVaultScreen(
                        isBengali = isBengali,
                        evidenceList = allEvidence,
                        onSelectCase = { id -> viewModel.selectCase(id) },
                        onDeleteEvidence = { id -> viewModel.deleteEvidenceItem(id) }
                    )
                }

                "official_reporting" -> {
                    OfficialReportingScreen(isBengali = isBengali)
                }

                "backup_restore" -> {
                    BackupRestoreScreen(
                        isBengali = isBengali,
                        totalCases = allCases.size,
                        totalEvidence = allEvidence.size,
                        onExportJson = { viewModel.exportBackupJson() },
                        onImportJson = { json, onSucc, onErr ->
                            viewModel.importBackupJson(json, onSucc, onErr)
                        },
                        onWipeAll = { viewModel.wipeAllData() }
                    )
                }

                "settings" -> {
                    SettingsScreen(
                        isBengali = isBengali,
                        onSetLanguage = { viewModel.setLanguage(it) },
                        hasPin = appPin.isNotEmpty(),
                        onSetPin = { viewModel.setPin(it) },
                        onRemovePin = { viewModel.removePin() },
                        onLockAppNow = { viewModel.lockApp() }
                    )
                }
            }
        }
    }
}
