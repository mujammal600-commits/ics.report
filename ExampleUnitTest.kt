package com.example

import com.example.analyzer.SmartEvidenceAnalyzer
import com.example.data.local.CaseEntity
import com.example.generator.ReportGenerator
import com.example.model.ReportCategoryRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testCategoryRegistryContainsAll20Categories() {
        assertEquals(20, ReportCategoryRegistry.categories.size)
        val impersonation = ReportCategoryRegistry.find("impersonation")
        assertEquals("Impersonation / Fake Identity", impersonation.nameEn)
        assertEquals("ছদ্মবেশ / ফেক পরিচয়", impersonation.nameBn)
    }

    @Test
    fun testSmartEvidenceAnalyzerEvaluation() {
        val result = SmartEvidenceAnalyzer.analyze(
            targetUrl = "https://facebook.com/fake.profile.123",
            description = "Someone stole my pictures and made a fake account to scam people for money using bKash.",
            platform = "Facebook",
            currentCategory = "impersonation",
            evidenceCount = 2,
            hasScreenshots = true,
            incidentDate = "02 Oct 2026"
        )

        assertTrue(result.readinessScore > 50)
        assertTrue(result.evidenceFound.isNotEmpty())
        assertNotNull(result.suggestedCategory)
    }

    @Test
    fun testReportGeneratorOutputs() {
        val testCase = CaseEntity(
            caseId = "ICS-2026-00001",
            targetUrl = "https://facebook.com/test",
            platform = "Facebook",
            category = "impersonation",
            priority = "High",
            status = "Ready to Report",
            incidentDate = "02 Oct 2026",
            description = "Fake account test description."
        )

        val bn = ReportGenerator.generateBengaliReport(testCase, emptyList(), emptyList())
        val en = ReportGenerator.generateEnglishReport(testCase, emptyList(), emptyList())

        assertTrue(bn.contains("ICS-2026-00001"))
        assertTrue(bn.contains("আইসিএস সাইবার রিপোর্ট প্রো"))
        assertTrue(en.contains("ICS-2026-00001"))
        assertTrue(en.contains("ICS CYBER REPORT PRO"))
    }
}
