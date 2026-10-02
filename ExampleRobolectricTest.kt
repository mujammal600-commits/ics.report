package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.BackupManager
import com.example.data.local.CaseEntity
import com.example.data.local.EvidenceEntity
import com.example.data.local.TimelineEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ICS Cyber Report Pro", appName)
    }

    @Test
    fun `test backup export and restore in Robolectric`() {
        val cases = listOf(
            CaseEntity(
                caseId = "ICS-2026-00099",
                targetUrl = "https://instagram.com/scam",
                platform = "Instagram",
                category = "scam",
                priority = "Critical",
                status = "Draft",
                incidentDate = "02 Oct 2026",
                description = "Scam test"
            )
        )
        val evidence = listOf(
            EvidenceEntity(
                caseId = "ICS-2026-00099",
                type = "Screenshot",
                title = "Evidence Test",
                contentOrUri = "uri_test"
            )
        )
        val timeline = listOf(
            TimelineEntity(
                caseId = "ICS-2026-00099",
                dateLabel = "02 Oct 2026",
                eventTitle = "Started"
            )
        )

        val json = BackupManager.exportToJson(cases, evidence, timeline)
        val restored = BackupManager.parseFromJson(json)

        assertEquals(1, restored.cases.size)
        assertEquals("ICS-2026-00099", restored.cases.first().caseId)
        assertEquals(1, restored.evidence.size)
        assertEquals(1, restored.timeline.size)
    }
}
