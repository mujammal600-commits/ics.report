package com.example.analyzer

import com.example.model.CategoryItem
import com.example.model.ReportCategoryRegistry

data class EvidenceAnalysisReport(
    val readinessScore: Int, // 0 to 100
    val strengthLevel: String, // Weak, Moderate, Strong, Solid
    val strengthLevelBn: String,
    val suggestedCategory: CategoryItem,
    val suggestedPriority: String,
    val evidenceFound: List<String>,
    val evidenceFoundBn: List<String>,
    val missingItems: List<String>,
    val missingItemsBn: List<String>,
    val actionableGuidance: List<String>,
    val actionableGuidanceBn: List<String>,
    val neutralityNoticeEn: String = "ICS Pro does not validate claims as factual; it organizes objective evidence for official review.",
    val neutralityNoticeBn: String = "এই অ্যাপ কোনো অভিযোগকে নিজে থেকে সত্য বলে ধরে নেয় না; তথ্যের ভিত্তিতে অফিশিয়াল রিপোর্টের জন্য প্রমাণ সাজায়।"
)

object SmartEvidenceAnalyzer {

    fun analyze(
        targetUrl: String,
        description: String,
        platform: String,
        currentCategory: String,
        evidenceCount: Int,
        hasScreenshots: Boolean,
        incidentDate: String
    ): EvidenceAnalysisReport {
        val found = mutableListOf<String>()
        val foundBn = mutableListOf<String>()
        val missing = mutableListOf<String>()
        val missingBn = mutableListOf<String>()
        val guidance = mutableListOf<String>()
        val guidanceBn = mutableListOf<String>()

        var score = 10

        // 1. Target URL Analysis
        val trimmedUrl = targetUrl.trim()
        if (trimmedUrl.isNotEmpty()) {
            if (trimmedUrl.startsWith("http://") || trimmedUrl.startsWith("https://")) {
                score += 25
                found.add("Valid Target URL provided ($trimmedUrl)")
                foundBn.add("সঠিক টার্গেট ইউআরএল যুক্ত আছে")
            } else {
                score += 15
                found.add("Target handle/URL entered without protocol ($trimmedUrl)")
                foundBn.add("টার্গেট আইডি/লিংক যুক্ত আছে")
                guidance.add("Add full https:// web address for target profile or post")
                guidanceBn.add("টার্গেটের পূর্ণাঙ্গ https:// লিংক ব্যবহার করুন")
            }
        } else {
            missing.add("Target URL or direct profile/post link is missing")
            missingBn.add("টার্গেট ইউআরএল বা প্রোফাইল লিংক অনুপস্থিত")
            guidance.add("Paste the exact URL of the offending account, post, or website")
            guidanceBn.add("অভিযুক্ত অ্যাকাউন্ট বা কনটেন্টের সরাসরি লিংক যুক্ত করুন")
        }

        // 2. Incident Description & Keywords
        val descTrimmed = description.trim()
        val descLength = descTrimmed.length
        val descLower = descTrimmed.lowercase()

        if (descLength >= 100) {
            score += 25
            found.add("Detailed incident narrative provided ($descLength characters)")
            foundBn.add("ঘটনার বিস্তারিত বিবরণ যুক্ত করা হয়েছে ($descLength অক্ষর)")
        } else if (descLength >= 20) {
            score += 15
            found.add("Brief description provided")
            foundBn.add("ঘটনার প্রাথমিক বিবরণ যুক্ত আছে")
            missing.add("In-depth details of what occurred, messages exchanged, or damages")
            missingBn.add("ঘটনার বিস্তারিত বিবরণ ও ক্ষতির পরিমাণ অনুপস্থিত")
            guidance.add("Expand description: explain who did what, exact timeline, and any financial/emotional loss")
            guidanceBn.add("কার সাথে কী ঘটেছে, কখন কী বার্তা পাঠিয়েছে তা বিস্তারিত লিখুন")
        } else {
            missing.add("Detailed factual incident narrative")
            missingBn.add("ঘটনার স্পষ্ট বিবরণ অনুপস্থিত")
            guidance.add("Write a factual description avoiding emotional speculation")
            guidanceBn.add("অনুমান ছাড়া বাস্তব ঘটনার স্পষ্ট বিবরণ লিখুন")
        }

        // 3. Evidence / Screenshots Analysis
        if (hasScreenshots || evidenceCount > 0) {
            if (evidenceCount >= 3) {
                score += 30
                found.add("Multiple supporting evidence items attached ($evidenceCount items)")
                foundBn.add("একাধিক প্রমাণপত্র ও স্ক্রিনশট যুক্ত ($evidenceCount টি)")
            } else {
                score += 20
                found.add("Primary screenshot / evidence attached ($evidenceCount item)")
                foundBn.add("প্রাথমিক প্রমাণ/স্ক্রিনশট সংরক্ষিত আছে ($evidenceCount টি)")
                guidance.add("Add additional uncropped screenshots showing sender name, timestamp, and context")
                guidanceBn.add("প্রেরকের নাম, সময় এবং আগের-পরের মেসেজ সহ আরও স্ক্রিনশট যোগ করুন")
            }
        } else {
            missing.add("No screenshots, chat logs, or transaction receipts attached")
            missingBn.add("কোনো স্ক্রিনশট, চ্যাটলগ বা ভাউচার যুক্ত করা হয়নি")
            guidance.add("Upload unedited screenshots of profile, messages, post, or payment receipts")
            guidanceBn.add("প্রোফাইল, মেসেজ বা পেমেন্ট ভাউচারের আন-এডিটেড স্ক্রিনশট আপলোড করুন")
        }

        // 4. Incident Date/Time
        if (incidentDate.isNotBlank()) {
            score += 10
            found.add("Incident timestamp recorded: $incidentDate")
            foundBn.add("ঘটনার তারিখ ও সময় উল্লেখ রয়েছে: $incidentDate")
        } else {
            missing.add("Precise incident date and time")
            missingBn.add("ঘটনার সুনির্দিষ্ট তারিখ ও সময় অনুপস্থিত")
            guidance.add("Record the exact date and approximate time the violation occurred")
            guidanceBn.add("ঘটনা ঘটার সুনির্দিষ্ট তারিখ ও আনুমানিক সময় নির্ধারণ করুন")
        }

        // 5. Keyword based category recommendation
        var suggestedCat = ReportCategoryRegistry.find(currentCategory)
        when {
            descLower.contains("bkash") || descLower.contains("বিকাশ") ||
            descLower.contains("nagad") || descLower.contains("নগদ") ||
            descLower.contains("টাকা") || descLower.contains("scam") ||
            descLower.contains("প্রতারক") || descLower.contains("পেমেন্ট") -> {
                suggestedCat = ReportCategoryRegistry.find("financial_scam")
            }
            descLower.contains("হ্যাক") || descLower.contains("hack") ||
            descLower.contains("পাসওয়ার্ড") || descLower.contains("otp") ||
            descLower.contains("লগইন") -> {
                suggestedCat = ReportCategoryRegistry.find("hacked")
            }
            descLower.contains("ব্ল্যাকমেইল") || descLower.contains("blackmail") ||
            descLower.contains("টাকা না দিলে") || descLower.contains("হুমকি") ||
            descLower.contains("extortion") -> {
                suggestedCat = ReportCategoryRegistry.find("extortion_blackmail")
            }
            descLower.contains("ফেক") || descLower.contains("fake") ||
            descLower.contains("নকল") || descLower.contains("ছদ্মবেশ") ||
            descLower.contains("impersonat") -> {
                suggestedCat = ReportCategoryRegistry.find("impersonation")
            }
            descLower.contains("ছবি") || descLower.contains("video") ||
            descLower.contains("ভিডিও") || descLower.contains("নগ্ন") ||
            descLower.contains("১৮+") || descLower.contains("viral") -> {
                suggestedCat = ReportCategoryRegistry.find("non_consensual_media")
            }
            descLower.contains("মার্ডার") || descLower.contains("হত্যা") ||
            descLower.contains("প্রাণনাশের") || descLower.contains("threat") -> {
                suggestedCat = ReportCategoryRegistry.find("threat_violence")
            }
        }

        // Suggest Priority
        val suggestedPriority = when (suggestedCat.id) {
            "threat_violence", "extortion_blackmail", "child_safety", "non_consensual_media", "hacked" -> "Critical"
            "financial_scam", "impersonation", "harassment", "phishing", "privacy_violation", "trademark_brand" -> "High"
            "fake_profile", "copyright_ip", "misinformation", "other" -> "Medium"
            else -> "Low"
        }

        val clampedScore = score.coerceIn(15, 100)
        val (strengthEn, strengthBn) = when {
            clampedScore >= 85 -> "Solid" to "সম্পূর্ণ ও সুদৃঢ়"
            clampedScore >= 65 -> "Strong" to "শক্তিশালী"
            clampedScore >= 45 -> "Moderate" to "মাঝারি"
            else -> "Weak" to "প্রাথমিক / দুর্বল"
        }

        return EvidenceAnalysisReport(
            readinessScore = clampedScore,
            strengthLevel = strengthEn,
            strengthLevelBn = strengthBn,
            suggestedCategory = suggestedCat,
            suggestedPriority = suggestedPriority,
            evidenceFound = found,
            evidenceFoundBn = foundBn,
            missingItems = missing,
            missingItemsBn = missingBn,
            actionableGuidance = guidance,
            actionableGuidanceBn = guidanceBn
        )
    }
}
