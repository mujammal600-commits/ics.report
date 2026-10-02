package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.ui.theme.CyberAccent
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan

@Composable
fun BackupRestoreScreen(
    isBengali: Boolean,
    totalCases: Int,
    totalEvidence: Int,
    onExportJson: () -> String,
    onImportJson: (String, () -> Unit, (String) -> Unit) -> Unit,
    onWipeAll: () -> Unit
) {
    val context = LocalContext.current
    var showImportDialog by remember { mutableStateOf(false) }
    var showWipeDialog by remember { mutableStateOf(false) }
    var jsonInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    fun shareBackup(json: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TITLE, "ICS_Report_Backup.json")
            putExtra(Intent.EXTRA_TEXT, json)
            type = "application/json"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Export ICS Backup")
        context.startActivity(shareIntent)
    }

    fun copyBackup(json: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("ICS Backup JSON", json)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, if (isBengali) "ব্যাকআপ JSON কপি করা হয়েছে" else "Backup JSON copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Column {
            Text(
                text = if (isBengali) "💾 ব্যাকআপ ও রিস্টোর" else "💾 Backup & Restore",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = if (isBengali)
                    "আপনার সমস্ত কেস এবং প্রমাণ নিরাপদ রাখতে ব্যাকআপ এক্সপোর্ট বা রিস্টোর করুন।"
                else
                    "Cases are stored safely in local database. Export backup before device reset.",
                fontSize = 12.sp,
                color = CyberAccent
            )
        }

        // Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberCardBg),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isBengali) "লোকাল ডাটাবেস স্থিতি" else "Local Database Status",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (isBengali) "মোট কেস: $totalCases টি | মোট প্রমাণপত্র: $totalEvidence টি" else "Total Cases: $totalCases | Total Evidence: $totalEvidence",
                        fontSize = 12.sp,
                        color = NeonCyan,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Icon(imageVector = Icons.Default.Archive, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(32.dp))
            }
        }

        // Export Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberCardBg),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBengali) "⬇️ ব্যাকআপ এক্সপোর্ট করুন (Export Backup)" else "⬇️ Export Backup",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = if (isBengali)
                        "সমস্ত কেস, প্রমাণাদি ও টাইমলাইন একটি নিরাপদ JSON ফাইলে তৈরি হবে।"
                    else
                        "Generates a complete offline JSON snapshot of all cases and evidence.",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val json = onExportJson()
                            shareBackup(json)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_backup_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isBengali) "শেয়ার / সেভ" else "Share JSON", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            val json = onExportJson()
                            copyBackup(json)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("copy_backup_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(if (isBengali) "কপি JSON" else "Copy JSON", fontSize = 12.sp, color = NeonCyan)
                    }
                }
            }
        }

        // Import Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberCardBg),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBengali) "⬆️ ব্যাকআপ রিস্টোর করুন (Import Backup)" else "⬆️ Restore Backup",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = if (isBengali)
                        "পূর্বে এক্সপোর্ট করা JSON পেস্ট করে সমস্ত ডাটা ফিরিয়ে আনুন।"
                    else
                        "Paste a previous JSON backup to restore cases and evidence items.",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                Button(
                    onClick = {
                        jsonInput = ""
                        statusMessage = null
                        showImportDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("open_import_dialog_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C2B4E)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.FileUpload, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isBengali) "JSON ব্যাকআপ পেস্ট করে রিস্টোর" else "Restore from JSON", color = NeonCyan, fontSize = 12.sp)
                }
            }
        }

        // Danger Zone: Wipe Local Data
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E0E11)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isBengali) "🗑️ সমস্ত লোকাল ডাটা মুছুন (Wipe Local Data)" else "🗑️ Wipe All Local Data",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEF4444),
                    fontSize = 14.sp
                )
                Text(
                    text = if (isBengali)
                        "সতর্কতা: এটি আপনার ডিভাইসে থাকা সমস্ত কেস এবং এভিডেন্স স্থায়ীভাবে ডিলিট করবে।"
                    else
                        "Warning: Permanently erases all saved cases and evidence from this device.",
                    fontSize = 11.sp,
                    color = Color(0xFFFCA5A5),
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                OutlinedButton(
                    onClick = { showWipeDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("wipe_all_data_button"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isBengali) "সমস্ত ডাটা মুছে ফেলুন" else "Delete All Local Data")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // Dialog: Import Backup
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = { Text(if (isBengali) "JSON ব্যাকআপ পেস্ট করুন" else "Paste Backup JSON") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = jsonInput,
                        onValueChange = { jsonInput = it },
                        placeholder = { Text("{\n  \"appName\": \"ICS Cyber Report Pro\",\n  \"cases\": [...]\n}") },
                        minLines = 6,
                        maxLines = 10,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CyberCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (statusMessage != null) {
                        Text(
                            text = statusMessage!!,
                            color = Color(0xFFEF4444),
                            fontSize = 11.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (jsonInput.isBlank()) {
                            statusMessage = if (isBengali) "দয়া করে JSON পেস্ট করুন" else "Please paste JSON string"
                            return@Button
                        }
                        onImportJson(
                            jsonInput,
                            {
                                showImportDialog = false
                                Toast.makeText(context, if (isBengali) "ডাটা সফলভাবে রিস্টোর হয়েছে!" else "Backup restored successfully!", Toast.LENGTH_SHORT).show()
                            },
                            { err ->
                                statusMessage = if (isBengali) "ভুল ফরম্যাট: $err" else "Error: $err"
                            }
                        )
                    }
                ) {
                    Text(if (isBengali) "রিস্টোর করুন" else "Restore")
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text(if (isBengali) "বাতিল" else "Cancel")
                }
            },
            containerColor = CyberCardBg
        )
    }

    // Dialog: Wipe Confirmation
    if (showWipeDialog) {
        AlertDialog(
            onDismissRequest = { showWipeDialog = false },
            title = { Text(if (isBengali) "নিশ্চিতভাবে সব মুছবেন?" else "Confirm Data Wipe") },
            text = {
                Text(
                    if (isBengali)
                        "এই ডিভাইসের সমস্ত কেস, প্রমাণপত্র ও টাইমলাইন চিরতরে মুছে যাবে। আপনি কি নিশ্চিত?"
                    else
                        "All cases and evidence will be permanently deleted from local storage. Are you sure?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showWipeDialog = false
                        onWipeAll()
                        Toast.makeText(context, if (isBengali) "সমস্ত ডাটা মুছে ফেলা হয়েছে" else "All data wiped", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text(if (isBengali) "হ্যাঁ, সব মুছে ফেলুন" else "Yes, Wipe All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWipeDialog = false }) {
                    Text(if (isBengali) "বাতিল" else "Cancel")
                }
            },
            containerColor = CyberCardBg
        )
    }
}
