package com.example.data

import com.example.data.local.CaseEntity
import com.example.data.local.EvidenceEntity
import com.example.data.local.TimelineEntity
import org.json.JSONArray
import org.json.JSONObject

data class BackupPayload(
    val exportDate: String,
    val appVersion: String,
    val cases: List<CaseEntity>,
    val evidence: List<EvidenceEntity>,
    val timeline: List<TimelineEntity>
)

object BackupManager {

    fun exportToJson(
        cases: List<CaseEntity>,
        evidence: List<EvidenceEntity>,
        timeline: List<TimelineEntity>
    ): String {
        val root = JSONObject()
        root.put("appName", "ICS Cyber Report Pro")
        root.put("version", "1.0")
        root.put("exportedAt", System.currentTimeMillis())

        val casesArray = JSONArray()
        cases.forEach { c ->
            val obj = JSONObject()
            obj.put("caseId", c.caseId)
            obj.put("targetUrl", c.targetUrl)
            obj.put("platform", c.platform)
            obj.put("category", c.category)
            obj.put("priority", c.priority)
            obj.put("status", c.status)
            obj.put("incidentDate", c.incidentDate)
            obj.put("description", c.description)
            obj.put("notes", c.notes)
            obj.put("followUpDate", c.followUpDate)
            obj.put("createdTimestamp", c.createdTimestamp)
            obj.put("updatedTimestamp", c.updatedTimestamp)
            casesArray.put(obj)
        }
        root.put("cases", casesArray)

        val evArray = JSONArray()
        evidence.forEach { ev ->
            val obj = JSONObject()
            obj.put("id", ev.id)
            obj.put("caseId", ev.caseId)
            obj.put("type", ev.type)
            obj.put("title", ev.title)
            obj.put("contentOrUri", ev.contentOrUri)
            obj.put("description", ev.description)
            obj.put("timestamp", ev.timestamp)
            evArray.put(obj)
        }
        root.put("evidence", evArray)

        val timelineArray = JSONArray()
        timeline.forEach { t ->
            val obj = JSONObject()
            obj.put("id", t.id)
            obj.put("caseId", t.caseId)
            obj.put("dateLabel", t.dateLabel)
            obj.put("eventTitle", t.eventTitle)
            obj.put("description", t.description)
            obj.put("timestamp", t.timestamp)
            timelineArray.put(obj)
        }
        root.put("timeline", timelineArray)

        return root.toString(2)
    }

    fun parseFromJson(jsonString: String): BackupPayload {
        val root = JSONObject(jsonString)
        val cases = mutableListOf<CaseEntity>()
        val evidence = mutableListOf<EvidenceEntity>()
        val timeline = mutableListOf<TimelineEntity>()

        if (root.has("cases")) {
            val casesArray = root.getJSONArray("cases")
            for (i in 0 until casesArray.length()) {
                val obj = casesArray.getJSONObject(i)
                cases.add(
                    CaseEntity(
                        caseId = obj.optString("caseId", "ICS-2026-00000"),
                        targetUrl = obj.optString("targetUrl", ""),
                        platform = obj.optString("platform", "Facebook"),
                        category = obj.optString("category", "other"),
                        priority = obj.optString("priority", "Medium"),
                        status = obj.optString("status", "Draft"),
                        incidentDate = obj.optString("incidentDate", ""),
                        description = obj.optString("description", ""),
                        notes = obj.optString("notes", ""),
                        followUpDate = obj.optString("followUpDate", ""),
                        createdTimestamp = obj.optLong("createdTimestamp", System.currentTimeMillis()),
                        updatedTimestamp = obj.optLong("updatedTimestamp", System.currentTimeMillis())
                    )
                )
            }
        }

        if (root.has("evidence")) {
            val evArray = root.getJSONArray("evidence")
            for (i in 0 until evArray.length()) {
                val obj = evArray.getJSONObject(i)
                evidence.add(
                    EvidenceEntity(
                        id = 0, // let Room re-index
                        caseId = obj.optString("caseId", ""),
                        type = obj.optString("type", "Screenshot"),
                        title = obj.optString("title", "Evidence"),
                        contentOrUri = obj.optString("contentOrUri", ""),
                        description = obj.optString("description", ""),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
        }

        if (root.has("timeline")) {
            val tArray = root.getJSONArray("timeline")
            for (i in 0 until tArray.length()) {
                val obj = tArray.getJSONObject(i)
                timeline.add(
                    TimelineEntity(
                        id = 0,
                        caseId = obj.optString("caseId", ""),
                        dateLabel = obj.optString("dateLabel", "Today"),
                        eventTitle = obj.optString("eventTitle", "Event"),
                        description = obj.optString("description", ""),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
        }

        return BackupPayload(
            exportDate = root.optString("exportedAt", ""),
            appVersion = root.optString("version", "1.0"),
            cases = cases,
            evidence = evidence,
            timeline = timeline
        )
    }
}
