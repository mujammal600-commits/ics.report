package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.local.EvidenceEntity
import com.example.ui.theme.CyberAccent
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.NeonCyan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EvidenceVaultScreen(
    isBengali: Boolean,
    evidenceList: List<EvidenceEntity>,
    onSelectCase: (String) -> Unit,
    onDeleteEvidence: (Long) -> Unit
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    var previewItem by remember { mutableStateOf<EvidenceEntity?>(null) }

    val filteredList = evidenceList.filter {
        selectedFilter == null || it.type.equals(selectedFilter, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isBengali) "🔎 এভিডেন্স ভল্ট (Evidence Vault)" else "🔎 Evidence Vault",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = if (isBengali) "মোট সংরক্ষিত প্রমাণ: ${evidenceList.size}টি" else "Total cataloged items: ${evidenceList.size}",
                    fontSize = 12.sp,
                    color = CyberAccent
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val filters = listOf(null to "All", "Screenshot" to "Screenshot", "Chat Log" to "Chats", "Payment Receipt" to "Receipts")
            filters.forEach { (typeKey, label) ->
                FilterChip(
                    selected = selectedFilter == typeKey,
                    onClick = { selectedFilter = typeKey },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                        selectedLabelColor = NeonCyan
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "📸", fontSize = 42.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isBengali) "কোনো প্রমাণ পাওয়া যায়নি" else "No evidence items found",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    Text(
                        text = if (isBengali) "নতুন কেস তৈরির সময় বা কেস ডিটেইলস থেকে প্রমাণ যোগ করুন" else "Attach screenshots or receipts from case details",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    val dateFormatted = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.US).format(Date(item.timestamp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
                            .clickable { previewItem = item }
                            .testTag("evidence_card_${item.id}"),
                        colors = CardDefaults.cardColors(containerColor = CyberCardBg),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icon or Image thumbnail
                            if (item.contentOrUri.startsWith("content://") || item.contentOrUri.startsWith("file://") || item.contentOrUri.endsWith(".jpg") || item.contentOrUri.endsWith(".png")) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF07111F))
                                        .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
                                ) {
                                    AsyncImage(
                                        model = item.contentOrUri,
                                        contentDescription = item.title,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF0F2642)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when {
                                            item.type.contains("Screenshot", ignoreCase = true) -> Icons.Default.Image
                                            item.type.contains("Receipt", ignoreCase = true) -> Icons.Default.Receipt
                                            item.type.contains("URL", ignoreCase = true) -> Icons.Default.Link
                                            else -> Icons.Default.Description
                                        },
                                        contentDescription = null,
                                        tint = NeonCyan,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = item.caseId,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = NeonCyan,
                                        modifier = Modifier.clickable { onSelectCase(item.caseId) }
                                    )
                                }

                                Text(
                                    text = "${item.type} • Added: $dateFormatted",
                                    fontSize = 11.sp,
                                    color = CyberAccent,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                if (item.description.isNotBlank()) {
                                    Text(
                                        text = item.description,
                                        fontSize = 11.sp,
                                        color = Color(0xFF94A3B8),
                                        maxLines = 1,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onDeleteEvidence(item.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(18.dp)
                                )
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

    // Detail Preview Dialog
    if (previewItem != null) {
        val item = previewItem!!
        AlertDialog(
            onDismissRequest = { previewItem = null },
            title = {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Case ID: ${item.caseId}", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(text = "Type: ${item.type}", color = CyberAccent, fontSize = 12.sp)
                    Text(text = "Reference: ${item.contentOrUri}", color = Color(0xFFE2E8F0), fontSize = 11.sp)
                    if (item.description.isNotBlank()) {
                        Text(text = "Notes: ${item.description}", color = Color(0xFF94A3B8), fontSize = 12.sp)
                    }

                    if (item.contentOrUri.startsWith("content://") || item.contentOrUri.startsWith("file://") || item.contentOrUri.endsWith(".jpg") || item.contentOrUri.endsWith(".png")) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
                        ) {
                            AsyncImage(
                                model = item.contentOrUri,
                                contentDescription = item.title,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val cid = item.caseId
                        previewItem = null
                        onSelectCase(cid)
                    }
                ) {
                    Text(if (isBengali) "কেসে যান" else "Open Case", color = NeonCyan)
                }
            },
            dismissButton = {
                TextButton(onClick = { previewItem = null }) {
                    Text(if (isBengali) "বন্ধ করুন" else "Close", color = Color(0xFF94A3B8))
                }
            },
            containerColor = CyberCardBg
        )
    }
}
