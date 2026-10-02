package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.CaseEntity
import com.example.data.local.EvidenceEntity
import com.example.data.local.TimelineEntity
import com.example.model.CasePriority
import com.example.model.CaseStatus
import com.example.model.PlatformRegistry
import com.example.model.ReportCategoryRegistry
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyberAccent
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseDetailScreen(
    isBengali: Boolean,
    case: CaseEntity,
    evidenceList: List<EvidenceEntity>,
    timelineList: List<TimelineEntity>,
    onUpdateStatus: (String) -> Unit,
    onUpdatePriority: (String) -> Unit,
    onUpdateNotes: (String) -> Unit,
    onSetFollowUpDate: (String) -> Unit,
    onAddEvidence: (title: String, type: String, uri: String, desc: String) -> Unit,
    onDeleteEvidence: (Long) -> Unit,
    onAddTimelineMilestone: (dateLabel: String, title: String, desc: String) -> Unit,
    onGenerateReport: () -> Unit,
    onOpenOfficialHelp: () -> Unit,
    onDeleteCase: () -> Unit
) {
    val context = LocalContext.current
    val categoryItem = ReportCategoryRegistry.find(case.category)

    var showStatusDialog by remember { mutableStateOf(false) }
    var showPriorityDialog by remember { mutableStateOf(false) }
    var showDeleteCaseDialog by remember { mutableStateOf(false) }
    var showAddEvidenceDialog by remember { mutableStateOf(false) }
    var showAddTimelineDialog by remember { mutableStateOf(false) }
    var showEditNotesDialog by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    // Evidence picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        uris.forEachIndexed { index, uri ->
            onAddEvidence("Screenshot #${evidenceList.size + index + 1}", "Screenshot", uri.toString(), "Preserved in Vault")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Case Header
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = case.caseId,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonCyan
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.clickable { showPriorityDialog = true }) {
                                PriorityBadge(priorityKey = case.priority, isBengali = isBengali)
                            }
                            Box(modifier = Modifier.clickable { showStatusDialog = true }) {
                                StatusBadge(statusKey = case.status, isBengali = isBengali)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${case.platform} • " + if (isBengali) categoryItem.nameBn else categoryItem.nameEn,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (case.targetUrl.isNotBlank()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = case.targetUrl,
                                color = CyberAccent,
                                fontSize = 12.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    val uri = if (case.targetUrl.startsWith("http")) case.targetUrl else "https://${case.targetUrl}"
                                    try {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                                    } catch (_: Exception) {}
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = "Open Link",
                                    tint = CyberAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "📅 " + (if (isBengali) "ঘটনার তারিখ: " else "Incident: ") + case.incidentDate.ifBlank { "N/A" },
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                        if (case.followUpDate.isNotBlank()) {
                            Text(
                                text = "🔔 " + (if (isBengali) "ফলো-আপ: " else "Follow-up: ") + case.followUpDate,
                                fontSize = 11.sp,
                                color = Color(0xFFEAB308),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Primary Action Buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onGenerateReport,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("case_detail_generate_report_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBengali) "📄 রিপোর্ট প্রস্তুত" else "📄 Report",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onOpenOfficialHelp,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("case_detail_official_reporting_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0C2B4E),
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBengali) "🔗 অফিশিয়াল রিপোর্ট" else "🔗 Official Help",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Incident Description Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isBengali) "ঘটনার পূর্ণ বিবরণ (Incident Narrative)" else "Incident Narrative",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = case.description.ifBlank { if (isBengali) "(কোনো বিবরণ দেওয়া হয়নি)" else "(No description provided)" },
                        fontSize = 13.sp,
                        color = Color(0xFFE2E8F0),
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Evidence Vault Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "📸 প্রমাণের ভল্ট (${evidenceList.size})" else "📸 Evidence Vault (${evidenceList.size})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Pick Image",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(
                                onClick = { showAddEvidenceDialog = true },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Evidence",
                                    tint = CyberAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    if (evidenceList.isEmpty()) {
                        Text(
                            text = if (isBengali) "কোনো প্রমাণ যুক্ত করা হয়নি। স্ক্রিনশট বা রসিদ যুক্ত করুন।" else "No evidence cataloged. Attach screenshots or receipts.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        evidenceList.forEachIndexed { idx, ev ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF07111F))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFF0F2642)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${idx + 1}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ev.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${ev.type} • ${ev.contentOrUri}",
                                        fontSize = 10.sp,
                                        color = Color(0xFF94A3B8),
                                        maxLines = 1
                                    )
                                }
                                IconButton(
                                    onClick = { onDeleteEvidence(ev.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Incident Timeline Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "📅 ঘটনার সময়ক্রম (Timeline)" else "📅 Incident Timeline",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        IconButton(
                            onClick = { showAddTimelineDialog = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Milestone",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    if (timelineList.isEmpty()) {
                        Text(
                            text = if (isBengali) "কোনো সময়ক্রম যুক্ত করা হয়নি।" else "No timeline milestones logged.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        timelineList.forEach { milestone ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "•",
                                    color = NeonCyan,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Column {
                                    Row {
                                        Text(
                                            text = "[${milestone.dateLabel}] ",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = CyberAccent
                                        )
                                        Text(
                                            text = milestone.eventTitle,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                    if (milestone.description.isNotBlank()) {
                                        Text(
                                            text = milestone.description,
                                            fontSize = 11.sp,
                                            color = Color(0xFF94A3B8)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Case Notes & Follow-up Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isBengali) "📝 তদন্ত নোট ও ফলো-আপ" else "📝 Notes & Follow-up",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )
                        IconButton(
                            onClick = { showEditNotesDialog = true },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Notes",
                                tint = CyberAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    if (case.notes.isNotBlank()) {
                        Text(
                            text = case.notes,
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    } else {
                        Text(
                            text = if (isBengali) "কোনো নোট লেখা হয়নি। ট্যাপ করে নোট বা ট্র্যাকিং আইডি যোগ করুন।" else "No private notes added. Tap to add tracking notes.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        // Danger Zone: Delete Case
        item {
            OutlinedButton(
                onClick = { showDeleteCaseDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("delete_case_button"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isBengali) "🗑️ কেসটি মুছে ফেলুন (Delete Case)" else "🗑️ Delete Case")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Dialog: Update Status
    if (showStatusDialog) {
        AlertDialog(
            onDismissRequest = { showStatusDialog = false },
            title = { Text(if (isBengali) "কেসের অবস্থা পরিবর্তন করুন" else "Update Case Status") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    CaseStatus.entries.forEach { status ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onUpdateStatus(status.key)
                                    showStatusDialog = false
                                }
                                .padding(vertical = 8.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(status.icon, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isBengali) status.titleBn else status.titleEn,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showStatusDialog = false }) {
                    Text(if (isBengali) "বাতিল" else "Cancel", color = NeonCyan)
                }
            },
            containerColor = CyberCardBg
        )
    }

    // Dialog: Update Priority
    if (showPriorityDialog) {
        AlertDialog(
            onDismissRequest = { showPriorityDialog = false },
            title = { Text(if (isBengali) "অগ্রাধিকার নির্ধারণ করুন" else "Change Priority") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    CasePriority.entries.forEach { p ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onUpdatePriority(p.key)
                                    showPriorityDialog = false
                                }
                                .padding(vertical = 8.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(p.icon, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isBengali) p.titleBn else p.titleEn,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPriorityDialog = false }) {
                    Text(if (isBengali) "বাতিল" else "Cancel", color = NeonCyan)
                }
            },
            containerColor = CyberCardBg
        )
    }

    // Dialog: Add Manual Evidence
    if (showAddEvidenceDialog) {
        var evTitle by remember { mutableStateOf("") }
        var evType by remember { mutableStateOf("Screenshot") }
        var evUri by remember { mutableStateOf("") }
        var evDesc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddEvidenceDialog = false },
            title = { Text(if (isBengali) "নতুন প্রমাণ যুক্ত করুন" else "Add New Evidence Item") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = evTitle,
                        onValueChange = { evTitle = it },
                        label = { Text(if (isBengali) "প্রমাণের নাম/শিরোনাম" else "Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = evType,
                        onValueChange = { evType = it },
                        label = { Text(if (isBengali) "ধরন (Screenshot/URL/Note/Receipt)" else "Type") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = evUri,
                        onValueChange = { evUri = it },
                        label = { Text(if (isBengali) "ফাইল লিংক বা রেফারেন্স" else "URI / Reference") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = evDesc,
                        onValueChange = { evDesc = it },
                        label = { Text(if (isBengali) "বর্ণনা" else "Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (evTitle.isNotBlank()) {
                            onAddEvidence(evTitle, evType, evUri, evDesc)
                            showAddEvidenceDialog = false
                        }
                    }
                ) {
                    Text(if (isBengali) "যুক্ত করুন" else "Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddEvidenceDialog = false }) {
                    Text(if (isBengali) "বাতিল" else "Cancel")
                }
            },
            containerColor = CyberCardBg
        )
    }

    // Dialog: Add Timeline Milestone
    if (showAddTimelineDialog) {
        val today = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date())
        var tDate by remember { mutableStateOf(today) }
        var tTitle by remember { mutableStateOf("") }
        var tDesc by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddTimelineDialog = false },
            title = { Text(if (isBengali) "সময়ক্রমের মাইলফলক যোগ করুন" else "Add Timeline Milestone") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = tDate,
                        onValueChange = { tDate = it },
                        label = { Text(if (isBengali) "তারিখ (যেমন: 02 Oct 2026)" else "Date") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tTitle,
                        onValueChange = { tTitle = it },
                        label = { Text(if (isBengali) "মাইলফলক শিরোনাম" else "Milestone Title") },
                        placeholder = { Text("e.g. Official report filed") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tDesc,
                        onValueChange = { tDesc = it },
                        label = { Text(if (isBengali) "বিবরণ (ঐচ্ছিক)" else "Details") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tTitle.isNotBlank()) {
                            onAddTimelineMilestone(tDate, tTitle, tDesc)
                            showAddTimelineDialog = false
                        }
                    }
                ) {
                    Text(if (isBengali) "সংরক্ষণ" else "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTimelineDialog = false }) {
                    Text(if (isBengali) "বাতিল" else "Cancel")
                }
            },
            containerColor = CyberCardBg
        )
    }

    // Dialog: Edit Notes
    if (showEditNotesDialog) {
        var tempNotes by remember { mutableStateOf(case.notes) }
        var tempFollowUp by remember { mutableStateOf(case.followUpDate) }

        AlertDialog(
            onDismissRequest = { showEditNotesDialog = false },
            title = { Text(if (isBengali) "নোট ও ফলো-আপ তারিখ সম্পাদনা" else "Edit Notes & Follow-up") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = tempFollowUp,
                        onValueChange = { tempFollowUp = it },
                        label = { Text(if (isBengali) "ফলো-আপ তারিখ" else "Follow-up Date") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tempNotes,
                        onValueChange = { tempNotes = it },
                        label = { Text(if (isBengali) "নোটস" else "Notes") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateNotes(tempNotes)
                        onSetFollowUpDate(tempFollowUp)
                        showEditNotesDialog = false
                    }
                ) {
                    Text(if (isBengali) "সংরক্ষণ" else "Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNotesDialog = false }) {
                    Text(if (isBengali) "বাতিল" else "Cancel")
                }
            },
            containerColor = CyberCardBg
        )
    }

    // Dialog: Confirm Delete
    if (showDeleteCaseDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteCaseDialog = false },
            title = { Text(if (isBengali) "কেস মুছে ফেলতে চান?" else "Delete Case?") },
            text = {
                Text(
                    if (isBengali)
                        "${case.caseId} এবং এর সাথে সংরক্ষিত সমস্ত প্রমাণপত্র ও টাইমলাইন স্থায়ীভাবে মুছে যাবে।"
                    else
                        "All evidence and timeline entries associated with ${case.caseId} will be permanently erased."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteCaseDialog = false
                        onDeleteCase()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text(if (isBengali) "হ্যাঁ, মুছে ফেলুন" else "Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteCaseDialog = false }) {
                    Text(if (isBengali) "বাতিল" else "Cancel")
                }
            },
            containerColor = CyberCardBg
        )
    }
}
