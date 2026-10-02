package com.example.generator

import com.example.data.local.CaseEntity
import com.example.data.local.EvidenceEntity
import com.example.data.local.TimelineEntity
import com.example.model.ReportCategoryRegistry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportGenerator {

    fun generateBengaliReport(
        case: CaseEntity,
        evidence: List<EvidenceEntity>,
        timeline: List<TimelineEntity>
    ): String {
        val catItem = ReportCategoryRegistry.find(case.category)
        val generatedDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale("bn", "BD")).format(Date())

        return buildString {
            appendLine("আইসিএস সাইবার রিপোর্ট প্রো (ICS CYBER REPORT PRO)")
            appendLine("অফিশিয়াল সাইবার ইনসিডেন্ট রিপোর্ট / অভিযোগের বিবরণ")
            appendLine("=".repeat(50))
            appendLine("কেস আইডি (Case ID): ${case.caseId}")
            appendLine("প্ল্যাটফর্ম (Platform): ${case.platform}")
            appendLine("অভিযোগের ধরন (Category): ${catItem.nameBn} (${catItem.nameEn})")
            appendLine("অগ্রাধিকার (Priority): ${case.priority}")
            appendLine("বর্তমান অবস্থা (Status): ${case.status}")
            appendLine("টার্গেট ইউআরএল/লিংক: ${case.targetUrl}")
            appendLine("ঘটনার তারিখ: ${if (case.incidentDate.isNotBlank()) case.incidentDate else "অনির্দিষ্ট"}")
            appendLine("রিপোর্ট প্রস্তুতের তারিখ: $generatedDate")
            appendLine("-".repeat(50))
            appendLine()
            appendLine("১. ঘটনার বিস্তারিত বিবরণ (Incident Description):")
            appendLine(case.description.ifBlank { "কোনো বিবরণ প্রদান করা হয়নি।" })
            appendLine()
            appendLine("২. সংগৃহীত প্রমাণের সারসংক্ষেপ (Evidence Summary):")
            if (evidence.isEmpty()) {
                appendLine("কোনো প্রমাণপত্র এখনো যুক্ত করা হয়নি।")
            } else {
                evidence.forEachIndexed { index, ev ->
                    val num = String.format("%02d", index + 1)
                    val evDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale("bn", "BD")).format(Date(ev.timestamp))
                    appendLine("  [প্রমাণ #$num] ${ev.title} (${ev.type})")
                    appendLine("    - সংযুক্তি/উৎস: ${ev.contentOrUri}")
                    if (ev.description.isNotBlank()) {
                        appendLine("    - বিবরণ: ${ev.description}")
                    }
                    appendLine("    - সংগ্রহের সময়: $evDate")
                }
            }
            appendLine()
            if (timeline.isNotEmpty()) {
                appendLine("৩. ঘটনার সময়ক্রম (Incident Chronology / Timeline):")
                timeline.forEach { event ->
                    appendLine("  • [${event.dateLabel}] ${event.eventTitle}")
                    if (event.description.isNotBlank()) {
                        appendLine("    ${event.description}")
                    }
                }
                appendLine()
            }
            if (case.notes.isNotBlank()) {
                appendLine("৪. অতিরিক্ত তথ্য ও নোটস (Investigator Notes):")
                appendLine(case.notes)
                appendLine()
            }
            appendLine("৫. প্রার্থিত প্রতিকার (Requested Action):")
            appendLine("  ক. উল্লিখিত ভুয়া/ক্ষতিকর লিংক ও কন্টেন্ট অবিলম্বে অপসারণ বা নিষ্ক্রিয়করণ।")
            appendLine("  খ. সংশ্লিষ্ট অপরাধীর আইপি ও সার্ভার অ্যাক্সেস লগ আইনি তদন্তের স্বার্থে সংরক্ষণ।")
            appendLine("  গ. প্ল্যাটফর্মের নীতিমালা ও প্রচলিত আইন অনুযায়ী প্রয়োজনীয় কঠোর ব্যবস্থা গ্রহণ।")
            appendLine()
            appendLine("ঘোষণা:")
            appendLine("এই রিপোর্টটি ব্যবহারকারীর সরবরাহকৃত বস্তুনিষ্ঠ প্রমাণের ভিত্তিতে তৈরি। এটি কোনো আদালত বা প্ল্যাটফর্মের চূড়ান্ত রায় নয়; আইনি প্রতিকার ও অফিশিয়াল অভিযোগ দাখিলের সহায়ক নথি।")
        }
    }

    fun generateEnglishReport(
        case: CaseEntity,
        evidence: List<EvidenceEntity>,
        timeline: List<TimelineEntity>
    ): String {
        val catItem = ReportCategoryRegistry.find(case.category)
        val generatedDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US).format(Date())

        return buildString {
            appendLine("ICS CYBER REPORT PRO - OFFICIAL INCIDENT DOSSIER")
            appendLine("Confidential Incident Documentation & Platform Abuse Report")
            appendLine("=".repeat(50))
            appendLine("Case Reference ID: ${case.caseId}")
            appendLine("Target Platform:   ${case.platform}")
            appendLine("Violation Type:    ${catItem.nameEn}")
            appendLine("Assigned Priority: ${case.priority}")
            appendLine("Workflow Status:   ${case.status}")
            appendLine("Target URL/Link:   ${case.targetUrl}")
            appendLine("Incident Date:     ${if (case.incidentDate.isNotBlank()) case.incidentDate else "Recorded at submission"}")
            appendLine("Generated At:      $generatedDate")
            appendLine("-".repeat(50))
            appendLine()
            appendLine("1. FACTUAL INCIDENT SUMMARY")
            appendLine(case.description.ifBlank { "No detailed narrative recorded." })
            appendLine()
            appendLine("2. EVIDENCE VAULT & SUPPORTING ATTACHMENTS")
            if (evidence.isEmpty()) {
                appendLine("No digital evidence items currently cataloged.")
            } else {
                evidence.forEachIndexed { index, ev ->
                    val num = String.format("%02d", index + 1)
                    val evDate = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US).format(Date(ev.timestamp))
                    appendLine("  Evidence #$num: [${ev.type.uppercase()}] - ${ev.title}")
                    appendLine("    Source / File Reference: ${ev.contentOrUri}")
                    if (ev.description.isNotBlank()) {
                        appendLine("    Notes: ${ev.description}")
                    }
                    appendLine("    Timestamp Recorded: $evDate")
                }
            }
            appendLine()
            if (timeline.isNotEmpty()) {
                appendLine("3. INCIDENT TIMELINE & SEQUENCE OF EVENTS")
                timeline.forEach { event ->
                    appendLine("  [${event.dateLabel}] ${event.eventTitle}")
                    if (event.description.isNotBlank()) {
                        appendLine("    Details: ${event.description}")
                    }
                }
                appendLine()
            }
            if (case.notes.isNotBlank()) {
                appendLine("4. INVESTIGATIVE & CASE NOTES")
                appendLine(case.notes)
                appendLine()
            }
            appendLine("5. FORMAL REQUESTED REMEDIES")
            appendLine("  a. Immediate take-down / termination of the offending account, post, or web address.")
            appendLine("  b. Preservation of access logs, IP identifiers, and transaction traces in accordance with applicable cyber law.")
            appendLine("  c. Escalation to the platform trust & safety enforcement division or cyber crime law enforcement.")
            appendLine()
            appendLine("DISCLAIMER:")
            appendLine("Generated via ICS Cyber Report Pro. This document collates factual evidence provided by the complainant and does not assume automatic legal culpability until formally adjudicated.")
        }
    }

    fun generatePrintableHtml(
        case: CaseEntity,
        evidence: List<EvidenceEntity>,
        timeline: List<TimelineEntity>
    ): String {
        val catItem = ReportCategoryRegistry.find(case.category)
        val generatedDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US).format(Date())

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <title>ICS Pro - ${case.caseId}</title>
            <style>
                body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; margin: 30px; color: #1e293b; line-height: 1.6; }
                .header { border-bottom: 3px solid #0284c7; padding-bottom: 12px; margin-bottom: 20px; }
                .title { font-size: 24px; font-weight: bold; color: #0f172a; margin: 0; }
                .subtitle { font-size: 13px; color: #64748b; margin-top: 4px; }
                .meta-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; font-size: 13px; }
                .meta-table td { padding: 8px 12px; border: 1px solid #cbd5e1; }
                .meta-label { font-weight: bold; background: #f8fafc; width: 28%; }
                .section-title { font-size: 16px; font-weight: bold; color: #0284c7; margin-top: 24px; margin-bottom: 8px; border-bottom: 1px solid #e2e8f0; padding-bottom: 4px; }
                .box { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 14px; font-size: 13px; white-space: pre-wrap; }
                .evidence-item { margin-bottom: 12px; padding: 10px; border-left: 3px solid #0284c7; background: #f8fafc; font-size: 13px; }
                .footer { margin-top: 30px; font-size: 11px; color: #94a3b8; border-top: 1px solid #e2e8f0; padding-top: 10px; }
            </style>
        </head>
        <body>
            <div class="header">
                <div class="title">ICS CYBER REPORT PRO - CASE DOSSIER</div>
                <div class="subtitle">Legitimate Incident Documentation & Official Evidence Summary • Date: $generatedDate</div>
            </div>

            <table class="meta-table">
                <tr>
                    <td class="meta-label">Case Reference ID</td>
                    <td><strong>${case.caseId}</strong></td>
                </tr>
                <tr>
                    <td class="meta-label">Target Platform</td>
                    <td>${case.platform}</td>
                </tr>
                <tr>
                    <td class="meta-label">Report Category</td>
                    <td>${catItem.nameEn} (${catItem.nameBn})</td>
                </tr>
                <tr>
                    <td class="meta-label">Target URL / Identifier</td>
                    <td>${case.targetUrl}</td>
                </tr>
                <tr>
                    <td class="meta-label">Assigned Priority</td>
                    <td>${case.priority}</td>
                </tr>
                <tr>
                    <td class="meta-label">Current Case Status</td>
                    <td>${case.status}</td>
                </tr>
                <tr>
                    <td class="meta-label">Incident Occurrence Date</td>
                    <td>${case.incidentDate.ifBlank { "Not specified" }}</td>
                </tr>
            </table>

            <div class="section-title">1. INCIDENT DESCRIPTION</div>
            <div class="box">${case.description.ifBlank { "No incident narrative provided." }}</div>

            <div class="section-title">2. EVIDENCE LOG & SUPPORTING FILES (${evidence.size} Items)</div>
            ${
                if (evidence.isEmpty()) "<p>No evidence items attached.</p>"
                else evidence.mapIndexed { idx, ev ->
                    """
                    <div class="evidence-item">
                        <strong>Evidence #${String.format("%02d", idx + 1)}: [${ev.type}] - ${ev.title}</strong><br>
                        Reference: ${ev.contentOrUri}<br>
                        ${if (ev.description.isNotBlank()) "Notes: ${ev.description}<br>" else ""}
                        Recorded: ${SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US).format(Date(ev.timestamp))}
                    </div>
                    """.trimIndent()
                }.joinToString("")
            }

            ${
                if (timeline.isNotEmpty()) {
                    """
                    <div class="section-title">3. INCIDENT TIMELINE</div>
                    <div class="box">
                    ${timeline.joinToString("\n") { "• [${it.dateLabel}] ${it.eventTitle}: ${it.description}" }}
                    </div>
                    """
                } else ""
            }

            <div class="section-title">4. REQUESTED ACTION</div>
            <div class="box">
1. Immediate review and take-down of the offending content/profile in compliance with Platform Community Guidelines.
2. Preservation of digital connection logs for potential criminal inquiry under Cyber Security legislation.
3. Notification of resolution to the reporting party.
            </div>

            <div class="footer">
                Document prepared automatically by ICS Cyber Report Pro. This is an objective factual dossier intended for official dispute resolution desks and legal authorities.
            </div>
        </body>
        </html>
        """.trimIndent()
    }
}
