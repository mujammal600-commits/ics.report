package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CaseEntity
import com.example.model.ReportCategoryRegistry
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyberAccent
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan

@Composable
fun HomeScreen(
    isBengali: Boolean,
    cases: List<CaseEntity>,
    onNavigate: (String) -> Unit,
    onSelectCase: (String) -> Unit
) {
    val totalCases = cases.size
    val openCases = cases.count { it.status !in listOf("Resolved", "Closed") }
    val readyCases = cases.count { it.status == "Ready to Report" }
    val resolvedCases = cases.count { it.status == "Resolved" }
    val pendingFollowUps = cases.filter { it.followUpDate.isNotBlank() && it.status !in listOf("Resolved", "Closed") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // Hero Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberCardBg),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(ElectricBlue, NeonCyan))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "ICS Cyber Report Pro",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (isBengali) "প্রফেশনাল সাইবার ইনসিডেন্ট ও রিপোর্ট ম্যানেজমেন্ট" else "Professional incident documentation & legitimate reporting assistant",
                                fontSize = 12.sp,
                                color = CyberAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onNavigate("new_report") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("hero_new_report_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBengali) "＋ নতুন রিপোর্ট তৈরি করুন (New Report)" else "＋ Create New Report",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = if (isBengali) "মোট কেস" else "Total Cases",
                    value = totalCases.toString(),
                    color = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = if (isBengali) "চলমান" else "Open",
                    value = openCases.toString(),
                    color = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = if (isBengali) "প্রস্তুত" else "Ready",
                    value = readyCases.toString(),
                    color = Color(0xFFF97316),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = if (isBengali) "নিষ্পত্তি" else "Resolved",
                    value = resolvedCases.toString(),
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Navigation Buttons (2 columns)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NavTile(
                        icon = Icons.Default.Folder,
                        title = if (isBengali) "আমার কেসসমূহ" else "My Cases",
                        subtitle = if (isBengali) "অনুসন্ধান ও ব্যবস্থাপনা" else "Search / Manage",
                        color = ElectricBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("cases") }
                    )
                    NavTile(
                        icon = Icons.Default.PhotoLibrary,
                        title = if (isBengali) "এভিডেন্স ভল্ট" else "Evidence Vault",
                        subtitle = if (isBengali) "স্ক্রিনশট ও প্রমাণ" else "Screenshots & Media",
                        color = NeonCyan,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("evidence_vault") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NavTile(
                        icon = Icons.Default.Link,
                        title = if (isBengali) "অফিশিয়াল রিপোর্ট" else "Official Portals",
                        subtitle = if (isBengali) "প্ল্যাটফর্ম হেল্পডেস্ক" else "Direct Official Help",
                        color = Color(0xFFA855F7),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("official_reporting") }
                    )
                    NavTile(
                        icon = Icons.Default.Archive,
                        title = if (isBengali) "ব্যাকআপ ও রিস্টোর" else "Backup & Restore",
                        subtitle = if (isBengali) "JSON ব্যাকআপ" else "Export & Import",
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate("backup_restore") }
                    )
                }
            }
        }

        // Follow up banner if any
        if (pendingFollowUps.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFEAB308).copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1C0E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = Color(0xFFEAB308),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBengali) "🔔 ফলো-আপ শিডিউল" else "🔔 Scheduled Follow-ups",
                                color = Color(0xFFFDE047),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (isBengali)
                                    "${pendingFollowUps.size}টি কেসের ফলো-আপ তারিখ নির্ধারিত রয়েছে"
                                else
                                    "${pendingFollowUps.size} case(s) have pending follow-up scheduled",
                                color = Color(0xFFE2E8F0),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Recent Cases Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBengali) "সাম্প্রতিক কেসসমূহ (Recent Cases)" else "Recent Cases",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = if (isBengali) "সব দেখুন" else "View All",
                    fontSize = 12.sp,
                    color = NeonCyan,
                    modifier = Modifier
                        .clickable { onNavigate("cases") }
                        .padding(4.dp)
                )
            }
        }

        // Recent cases list (top 4)
        if (cases.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isBengali) "কোনো কেস এখনো সংরক্ষণ করা হয়নি।" else "No cases recorded yet.",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(cases.take(4)) { item ->
                CaseSummaryCard(
                    case = item,
                    isBengali = isBengali,
                    onClick = { onSelectCase(item.caseId) }
                )
            }
        }

        // Regulatory & Neutrality notice
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF091625))
                    .border(1.dp, Color(0xFF1E3553), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = if (isBengali)
                        "⚠️ গুরুত্বপূর্ণ নির্দেশিকা: ICS Pro প্ল্যাটফর্ম কোনো অভিযোগকে নিজে থেকে সত্য বলে ধরে নেয় না। এটি আইনি প্রমাণাদি সুশৃঙ্খলভাবে সংগ্রহ ও অফিশিয়াল অভিযোগের জন্য রিপোর্ট প্রস্তুত করে। কোনো ভুয়া বা অসত্য অভিযোগ করবেন না।"
                    else
                        "⚠️ Neutrality Notice: ICS Pro organizes factual evidence and prepares legitimate reports. It does not perform mass, fake or automated reporting. Always verify facts before submitting to official authorities.",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, CyberCardBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberCardBg),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = Color(0xFF94A3B8),
                maxLines = 1
            )
        }
    }
}

@Composable
fun NavTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CyberCardBg),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Composable
fun CaseSummaryCard(
    case: CaseEntity,
    isBengali: Boolean,
    onClick: () -> Unit
) {
    val categoryItem = ReportCategoryRegistry.find(case.category)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag("case_item_${case.caseId}"),
        colors = CardDefaults.cardColors(containerColor = CyberCardBg),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = categoryItem.icon, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = case.caseId,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
                PriorityBadge(priorityKey = case.priority, isBengali = isBengali)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${case.platform} • " + if (isBengali) categoryItem.nameBn else categoryItem.nameEn,
                color = CyberAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            if (case.targetUrl.isNotBlank()) {
                Text(
                    text = case.targetUrl,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(statusKey = case.status, isBengali = isBengali)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isBengali) "বিস্তারিত" else "Details",
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
