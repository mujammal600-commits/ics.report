package com.example.model

import androidx.compose.ui.graphics.Color

enum class CaseStatus(
    val key: String,
    val titleEn: String,
    val titleBn: String,
    val badgeColor: Long,
    val icon: String
) {
    DRAFT("Draft", "Draft", "খসড়া", 0xFFEAB308, "🟡"),
    EVIDENCE_COLLECTING("Evidence Collecting", "Evidence Collecting", "প্রমাণ সংগ্রহ", 0xFF38BDF8, "🔵"),
    READY_TO_REPORT("Ready to Report", "Ready to Report", "রিপোর্ট প্রস্তুত", 0xFFF97316, "🟠"),
    REPORT_SUBMITTED("Report Submitted", "Report Submitted", "রিপোর্ট দাখিল", 0xFFA855F7, "🟣"),
    RESOLVED("Resolved", "Resolved", "সমাধান হয়েছে", 0xFF10B981, "🟢"),
    REJECTED("Rejected", "Rejected", "প্রত্যাখ্যাত", 0xFFEF4444, "🔴"),
    CLOSED("Closed", "Closed", "বন্ধ", 0xFF64748B, "⚫");

    companion object {
        fun fromKey(key: String?): CaseStatus =
            entries.firstOrNull { it.key.equals(key, ignoreCase = true) || it.name.equals(key, ignoreCase = true) }
                ?: DRAFT
    }
}

enum class CasePriority(
    val key: String,
    val titleEn: String,
    val titleBn: String,
    val color: Long,
    val icon: String
) {
    CRITICAL("Critical", "Critical", "জরুরি", 0xFFEF4444, "🔴"),
    HIGH("High", "High", "উচ্চ", 0xFFF97316, "🟠"),
    MEDIUM("Medium", "Medium", "মাঝারি", 0xFFEAB308, "🟡"),
    LOW("Low", "Low", "সাধারণ", 0xFF10B981, "🟢");

    companion object {
        fun fromKey(key: String?): CasePriority =
            entries.firstOrNull { it.key.equals(key, ignoreCase = true) || it.name.equals(key, ignoreCase = true) }
                ?: MEDIUM
    }
}
