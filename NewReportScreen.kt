package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Save
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.analyzer.EvidenceAnalysisReport
import com.example.analyzer.SmartEvidenceAnalyzer
import com.example.model.CasePriority
import com.example.model.PlatformRegistry
import com.example.model.ReportCategoryRegistry
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
fun NewReportScreen(
    isBengali: Boolean,
    nextCaseId: String,
    onSaveCase: (
        targetUrl: String,
        platform: String,
        category: String,
        priority: String,
        incidentDate: String,
        description: String,
        notes: String,
        followUpDate: String,
        evidence: List<Pair<String, String>>
    ) -> Unit,
    onCancel: () -> Unit
) {
    val platforms = PlatformRegistry.platforms.map { it.name }
    val categories = ReportCategoryRegistry.categories
    val priorities = CasePriority.entries

    var selectedPlatform by remember { mutableStateOf(platforms.first()) }
    var targetUrl by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.first().id) }
    var selectedPriority by remember { mutableStateOf(CasePriority.MEDIUM.key) }
    var incidentDate by remember {
        mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date()))
    }
    var followUpDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val attachedUris = remember { mutableStateListOf<Uri>() }

    var isPlatformExpanded by remember { mutableStateOf(false) }
    var isCategoryExpanded by remember { mutableStateOf(false) }
    var isPriorityExpanded by remember { mutableStateOf(false) }

    var analysisResult by remember { mutableStateOf<EvidenceAnalysisReport?>(null) }
    var showUrlError by remember { mutableStateOf(false) }

    // Modern zero-permission Android Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris: List<Uri> ->
        attachedUris.addAll(uris)
    }

    val currentCatItem = ReportCategoryRegistry.find(selectedCategory)

    fun performAnalysis() {
        val res = SmartEvidenceAnalyzer.analyze(
            targetUrl = targetUrl,
            description = description,
            platform = selectedPlatform,
            currentCategory = selectedCategory,
            evidenceCount = attachedUris.size,
            hasScreenshots = attachedUris.isNotEmpty(),
            incidentDate = incidentDate
        )
        analysisResult = res
        // Auto apply suggested priority if user hasn't explicitly customized
        selectedPriority = res.suggestedPriority
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isBengali) "🎯 নতুন কেস তৈরি করুন" else "🎯 Create New Case",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isBengali) "কেস আইডি: $nextCaseId" else "Case ID: $nextCaseId",
                        fontSize = 12.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Platform & Priority Selection
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Platform dropdown
                ExposedDropdownMenuBox(
                    expanded = isPlatformExpanded,
                    onExpandedChange = { isPlatformExpanded = !isPlatformExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedPlatform,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isBengali) "প্ল্যাটফর্ম" else "Platform") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPlatformExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = CyberCardBg,
                            unfocusedContainerColor = CyberCardBg
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("platform_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = isPlatformExpanded,
                        onDismissRequest = { isPlatformExpanded = false },
                        modifier = Modifier.background(CyberCardBg)
                    ) {
                        platforms.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p, color = Color.White) },
                                onClick = {
                                    selectedPlatform = p
                                    isPlatformExpanded = false
                                }
                            )
                        }
                    }
                }

                // Priority dropdown
                ExposedDropdownMenuBox(
                    expanded = isPriorityExpanded,
                    onExpandedChange = { isPriorityExpanded = !isPriorityExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    val pObj = CasePriority.fromKey(selectedPriority)
                    OutlinedTextField(
                        value = "${pObj.icon} " + if (isBengali) pObj.titleBn else pObj.titleEn,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(if (isBengali) "অগ্রাধিকার" else "Priority") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPriorityExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = CyberCardBg,
                            unfocusedContainerColor = CyberCardBg
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("priority_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = isPriorityExpanded,
                        onDismissRequest = { isPriorityExpanded = false },
                        modifier = Modifier.background(CyberCardBg)
                    ) {
                        priorities.forEach { prio ->
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        Text(prio.icon)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(if (isBengali) prio.titleBn else prio.titleEn, color = Color.White)
                                    }
                                },
                                onClick = {
                                    selectedPriority = prio.key
                                    isPriorityExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Target URL field
        item {
            OutlinedTextField(
                value = targetUrl,
                onValueChange = {
                    targetUrl = it
                    showUrlError = false
                },
                label = { Text(if (isBengali) "টার্গেট ইউআরএল / প্রোফাইল লিংক *" else "Target URL / Profile Link *") },
                placeholder = { Text("https://facebook.com/profile.php?id=... or handle") },
                isError = showUrlError,
                supportingText = {
                    if (showUrlError) {
                        Text(
                            text = if (isBengali) "টার্গেট ইউআরএল প্রদান করা আবশ্যক" else "Target URL is required",
                            color = Color(0xFFEF4444)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = CyberCardBg,
                    unfocusedContainerColor = CyberCardBg
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("target_url_input")
            )
        }

        // Category dropdown (20 categories)
        item {
            ExposedDropdownMenuBox(
                expanded = isCategoryExpanded,
                onExpandedChange = { isCategoryExpanded = !isCategoryExpanded }
            ) {
                OutlinedTextField(
                    value = "${currentCatItem.icon} " + if (isBengali) currentCatItem.nameBn else currentCatItem.nameEn,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(if (isBengali) "রিপোর্ট ক্যাটাগরি (Report Category)" else "Report Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = CyberCardBg,
                        unfocusedContainerColor = CyberCardBg
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .testTag("category_dropdown")
                )
                ExposedDropdownMenu(
                    expanded = isCategoryExpanded,
                    onDismissRequest = { isCategoryExpanded = false },
                    modifier = Modifier.background(CyberCardBg)
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(cat.icon, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isBengali) cat.nameBn else cat.nameEn,
                                        color = Color.White,
                                        fontSize = 13.sp
                                    )
                                }
                            },
                            onClick = {
                                selectedCategory = cat.id
                                isCategoryExpanded = false
                            }
                        )
                    }
                }
            }

            // Category guidance hint
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0C1F33))
                    .padding(8.dp)
            ) {
                Text(
                    text = "💡 " + if (isBengali) currentCatItem.guidanceBn else currentCatItem.guidanceEn,
                    fontSize = 11.sp,
                    color = CyberAccent,
                    lineHeight = 15.sp
                )
            }
        }

        // Incident Date & Follow-up Date
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = incidentDate,
                    onValueChange = { incidentDate = it },
                    label = { Text(if (isBengali) "ঘটনার তারিখ" else "Incident Date") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = CyberCardBg,
                        unfocusedContainerColor = CyberCardBg
                    ),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = followUpDate,
                    onValueChange = { followUpDate = it },
                    label = { Text(if (isBengali) "ফলো-আপ তারিখ" else "Follow-up Date") },
                    placeholder = { Text("05 Oct 2026") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = CyberCardBg,
                        unfocusedContainerColor = CyberCardBg
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Description Field
        item {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(if (isBengali) "ঘটনার বিস্তারিত বিবরণ (Description) *" else "Incident Description *") },
                placeholder = {
                    Text(
                        if (isBengali)
                            "ঘটনার শুরু কীভাবে, কী ক্ষতি হয়েছে, কোনো বার্তা/হুমকি দেওয়া হয়েছে কিনা বিস্তারিত লিখুন।"
                        else
                            "Describe what occurred, who communicated, what was demanded or posted, and factual timeline."
                    )
                },
                minLines = 4,
                maxLines = 8,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CyberCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = CyberCardBg,
                    unfocusedContainerColor = CyberCardBg
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("description_input")
            )
        }

        // Evidence Vault Attachment Section
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
                            text = if (isBengali) "📸 প্রমাণপত্র ও স্ক্রিনশট (Evidence Vault)" else "📸 Evidence & Screenshots",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("attach_evidence_button")
                        ) {
                            Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null, tint = NeonCyan)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBengali) "সংযুক্তি" else "Attach",
                                color = NeonCyan,
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (attachedUris.isEmpty()) {
                        Text(
                            text = if (isBengali)
                                "কোনো প্রমাণ যুক্ত করা হয়নি। প্রোফাইল বা চ্যাটলগের স্ক্রিনশট যুক্ত করুন।"
                            else
                                "No evidence attached yet. Tap 'Attach' to pick unedited screenshots.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(attachedUris) { uri ->
                                Box(
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
                                ) {
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = "Evidence Preview",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                    IconButton(
                                        onClick = { attachedUris.remove(uri) },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(24.dp)
                                            .background(Color.Black.copy(alpha = 0.6f))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Smart Evidence Analyzer Card (Input/Output as requested in Point 2)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF091629)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBengali) "স্মার্ট এভিডেন্স অ্যানালাইজার" else "Smart Evidence Analyzer",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = NeonCyan
                            )
                        }
                        Button(
                            onClick = { performAnalysis() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C2B4E)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("run_analysis_button")
                        ) {
                            Text(
                                text = if (isBengali) "🔎 বিশ্লেষণ করুন" else "🔎 Analyze",
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (analysisResult != null) {
                        val res = analysisResult!!
                        Spacer(modifier = Modifier.height(12.dp))

                        // Readiness Progress Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (isBengali) "রিপোর্ট প্রস্তুতি (Readiness): ${res.readinessScore}%" else "Report Readiness: ${res.readinessScore}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isBengali) "প্রমাণের সক্ষমতা: ${res.strengthLevelBn}" else "Strength: ${res.strengthLevel}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (res.readinessScore >= 70) Color(0xFF10B981) else Color(0xFFF59E0B)
                            )
                        }

                        LinearProgressIndicator(
                            progress = { res.readinessScore / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (res.readinessScore >= 70) Color(0xFF10B981) else Color(0xFF00E5FF),
                            trackColor = Color(0xFF1E3553)
                        )

                        // Found Evidence list
                        if (res.evidenceFound.isNotEmpty()) {
                            Text(
                                text = if (isBengali) "✅ বিদ্যমান প্রমাণাদি (Evidence Found):" else "✅ Evidence Found:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF10B981),
                                modifier = Modifier.padding(top = 6.dp)
                            )
                            (if (isBengali) res.evidenceFoundBn else res.evidenceFound).forEach { item ->
                                Text(text = "• $item", fontSize = 11.sp, color = Color(0xFFE2E8F0))
                            }
                        }

                        // Missing info list
                        if (res.missingItems.isNotEmpty()) {
                            Text(
                                text = if (isBengali) "⚠️ যা যা অনুপস্থিত (Missing Information):" else "⚠️ Missing Information:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF97316),
                                modifier = Modifier.padding(top = 6.dp)
                            )
                            (if (isBengali) res.missingItemsBn else res.missingItems).forEach { item ->
                                Text(text = "• $item", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                            }
                        }

                        // Guidance: কী কী প্রমাণ আরও প্রয়োজন
                        if (res.actionableGuidance.isNotEmpty()) {
                            Text(
                                text = if (isBengali) "📋 কী কী প্রমাণ আরও প্রয়োজন (Actionable Recommendations):" else "📋 Actionable Recommendations:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NeonCyan,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                            (if (isBengali) res.actionableGuidanceBn else res.actionableGuidance).forEach { item ->
                                Text(text = "→ $item", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                        }
                    } else {
                        Text(
                            text = if (isBengali)
                                "প্রয়োজনীয় তথ্য ও প্রমাণ যুক্ত করে 'বিশ্লেষণ করুন' বাটনে চাপুন। অ্যাপ প্রমাণের শক্তি ও অনুপস্থিত তথ্য নির্ণয় করবে।"
                            else
                                "Fill in details and tap 'Analyze' to evaluate evidence strength, suggested category, and readiness.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }

        // Action Buttons
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("cancel_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = if (isBengali) "বাতিল" else "Cancel", color = Color(0xFF94A3B8))
                }

                Button(
                    onClick = {
                        if (targetUrl.isBlank()) {
                            showUrlError = true
                            return@Button
                        }
                        val evList = attachedUris.mapIndexed { idx, u ->
                            Pair("Screenshot #${idx + 1}", u.toString())
                        }
                        onSaveCase(
                            targetUrl,
                            selectedPlatform,
                            selectedCategory,
                            selectedPriority,
                            incidentDate,
                            description,
                            notes,
                            followUpDate,
                            evList
                        )
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("save_case_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBengali) "💾 কেস সংরক্ষণ করুন" else "💾 Save Case",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}
