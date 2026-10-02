package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.local.CaseEntity
import com.example.model.CasePriority
import com.example.model.CaseStatus
import com.example.model.ReportCategoryRegistry
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.theme.CyberAccent
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.NeonCyan

@Composable
fun CaseListScreen(
    isBengali: Boolean,
    cases: List<CaseEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedStatus: String?,
    onStatusFilterChange: (String?) -> Unit,
    selectedPriority: String?,
    onPriorityFilterChange: (String?) -> Unit,
    onSelectCase: (String) -> Unit
) {
    val statuses = CaseStatus.entries
    val priorities = CasePriority.entries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(if (isBengali) "কেস আইডি, ইউআরএল বা ক্যাটাগরি অনুসন্ধান করুন..." else "Search Case ID, URL, category, keywords...") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NeonCyan) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = Color(0xFF94A3B8))
                    }
                }
            },
            singleLine = true,
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
                .testTag("case_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal Status Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedStatus == null,
                onClick = { onStatusFilterChange(null) },
                label = { Text(if (isBengali) "সকল অবস্থা" else "All Statuses", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                    selectedLabelColor = NeonCyan
                )
            )

            statuses.forEach { s ->
                FilterChip(
                    selected = selectedStatus == s.key,
                    onClick = {
                        onStatusFilterChange(if (selectedStatus == s.key) null else s.key)
                    },
                    label = { Text("${s.icon} " + if (isBengali) s.titleBn else s.titleEn, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(s.badgeColor).copy(alpha = 0.25f),
                        selectedLabelColor = Color(s.badgeColor)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Horizontal Priority Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedPriority == null,
                onClick = { onPriorityFilterChange(null) },
                label = { Text(if (isBengali) "সকল অগ্রাধিকার" else "All Priorities", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = CyberAccent.copy(alpha = 0.2f),
                    selectedLabelColor = CyberAccent
                )
            )

            priorities.forEach { p ->
                FilterChip(
                    selected = selectedPriority == p.key,
                    onClick = {
                        onPriorityFilterChange(if (selectedPriority == p.key) null else p.key)
                    },
                    label = { Text("${p.icon} " + if (isBengali) p.titleBn else p.titleEn, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(p.color).copy(alpha = 0.25f),
                        selectedLabelColor = Color(p.color)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (isBengali) "ফলাফল: ${cases.size}টি কেস পাওয়া গেছে" else "Showing ${cases.size} case(s)",
            fontSize = 12.sp,
            color = Color(0xFF94A3B8),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        // Case List
        if (cases.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📁", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isBengali) "কোনো কেস পাওয়া যায়নি" else "No matching cases found",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = if (isBengali) "ফিল্টার পরিবর্তন করুন বা নতুন রিপোর্ট তৈরি করুন" else "Try clearing filters or create a new report",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cases, key = { it.caseId }) { c ->
                    val catItem = ReportCategoryRegistry.find(c.category)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
                            .clickable { onSelectCase(c.caseId) }
                            .testTag("case_row_${c.caseId}"),
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
                                    Text(text = catItem.icon, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = c.caseId,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                                PriorityBadge(priorityKey = c.priority, isBengali = isBengali)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${c.platform} • " + if (isBengali) catItem.nameBn else catItem.nameEn,
                                color = CyberAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )

                            if (c.targetUrl.isNotBlank()) {
                                Text(
                                    text = c.targetUrl,
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            if (c.description.isNotBlank()) {
                                Text(
                                    text = c.description,
                                    color = Color(0xFFE2E8F0),
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(statusKey = c.status, isBengali = isBengali)
                                if (c.incidentDate.isNotBlank()) {
                                    Text(
                                        text = "📅 ${c.incidentDate}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
    }
}
