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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlatformInfo
import com.example.model.PlatformRegistry
import com.example.ui.theme.CyberAccent
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan

@Composable
fun OfficialReportingScreen(
    isBengali: Boolean
) {
    val context = LocalContext.current
    val platforms = PlatformRegistry.platforms
    var expandedPlatform by remember { mutableStateOf<String?>(null) }

    fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = if (isBengali) "🔗 অফিশিয়াল রিপোর্টিং সেন্টার" else "🔗 Official Reporting Center",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = if (isBengali)
                        "প্ল্যাটফর্ম সমূহের অফিশিয়াল নীতি অনুযায়ী বৈধ অভিযোগ দাখিলের সরাসরি সংযোগ।"
                    else
                        "Direct routes to official platform abuse & trust safety desks.",
                    fontSize = 12.sp,
                    color = CyberAccent
                )
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF091629))
                    .border(1.dp, Color(0xFF1E3553), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = if (isBengali)
                        "⚠️ গুরুত্বপূর্ণ নীতি: এই অ্যাপ নিজে শত শত ভুয়া বা স্প্যাম রিপোর্ট পাঠায় না। এটি আপনাকে অফিশিয়াল রিপোর্টিং প্রক্রিয়ায় নিয়ে যায় যাতে সঠিক প্রমাণ সহ দ্রুত ব্যবস্থা নেওয়া হয়।"
                    else
                        "⚠️ Note: ICS Pro does not mass-report or spam platforms. It guides you to the authentic reporting route with verified evidence to ensure highest resolution success.",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    lineHeight = 16.sp
                )
            }
        }

        items(platforms, key = { it.name }) { p ->
            val isExpanded = expandedPlatform == p.name

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp))
                    .clickable { expandedPlatform = if (isExpanded) null else p.name }
                    .testTag("official_tile_${p.name}"),
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
                            Text(text = p.iconEmoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = p.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isBengali) p.descriptionBn else p.descriptionEn,
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8),
                                    maxLines = if (isExpanded) 4 else 1
                                )
                            }
                        }
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (isBengali) "📋 রিপোর্ট করার ধাপসমূহ:" else "📋 Reporting Process:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan
                        )

                        val steps = if (isBengali) p.stepsBn else p.stepsEn
                        steps.forEachIndexed { idx, step ->
                            Text(
                                text = "${idx + 1}. $step",
                                fontSize = 11.sp,
                                color = Color(0xFFE2E8F0),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { openUrl(p.reportUrl) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBengali) "রিপোর্ট লিঙ্ক খুলুন" else "Open Report Form",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = { openUrl(p.helpCenterUrl) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (isBengali) "হেল্প সেন্টার" else "Help Center",
                                    fontSize = 11.sp,
                                    color = NeonCyan
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
