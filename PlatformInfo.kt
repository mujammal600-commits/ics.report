package com.example.model

data class PlatformInfo(
    val name: String,
    val iconEmoji: String,
    val reportUrl: String,
    val helpCenterUrl: String,
    val descriptionEn: String,
    val descriptionBn: String,
    val stepsEn: List<String>,
    val stepsBn: List<String>
)

object PlatformRegistry {
    val platforms = listOf(
        PlatformInfo(
            name = "Facebook",
            iconEmoji = "📘",
            reportUrl = "https://www.facebook.com/help/report/",
            helpCenterUrl = "https://www.facebook.com/help/",
            descriptionEn = "Official Meta Facebook abuse, impersonation & safety reporting desk.",
            descriptionBn = "মেটা ফেসবুকের ভুয়া অ্যাকাউন্ট, হয়রানি ও সাইবার অপরাধ সংক্রান্ত অফিশিয়াল রিপোর্ট সেন্টার।",
            stepsEn = listOf(
                "Go to target Profile / Post / Page",
                "Tap the 3 dots (...) menu on the top right",
                "Select 'Find support or report profile/post'",
                "Choose the exact matching category",
                "Attach ICS Pro generated evidence and submit"
            ),
            stepsBn = listOf(
                "টার্গেট প্রোফাইল বা পোস্টের ৩টি ডট (...) বাটনে চাপুন",
                "'Find support or report profile' নির্বাচন করুন",
                "সঠিক অভিযোগের ধরন বেছে নিন",
                "আইসিএস রিপোর্ট প্রোর প্রস্তুতকৃত তথ্য উল্লেখ করুন"
            )
        ),
        PlatformInfo(
            name = "Instagram",
            iconEmoji = "📸",
            reportUrl = "https://help.instagram.com/contact/636276399721841",
            helpCenterUrl = "https://help.instagram.com/",
            descriptionEn = "Instagram official support for impersonation, hacked accounts and harassment.",
            descriptionBn = "ইনস্টাগ্রাম অ্যাকাউন্ট হ্যাক, ছদ্মবেশ ও হয়রানি সংক্রান্ত অফিশিয়াল রিপোর্ট।",
            stepsEn = listOf(
                "Open user's Instagram profile",
                "Tap the 3 dots menu in top corner",
                "Tap 'Report' -> 'Report Account'",
                "Select 'It's pretending to be someone else' or other violation",
                "Follow on-screen instructions"
            ),
            stepsBn = listOf(
                "ইনস্টাগ্রাম প্রোফাইলের ৩টি ডট মেনু খুলুন",
                "'Report' থেকে 'Report Account' নির্বাচন করুন",
                "ছদ্মবেশ বা হয়রানির ধরনটি বেছে নিন"
            )
        ),
        PlatformInfo(
            name = "WhatsApp",
            iconEmoji = "💬",
            reportUrl = "https://www.whatsapp.com/contact/",
            helpCenterUrl = "https://faq.whatsapp.com/593259925979514",
            descriptionEn = "Report scam numbers, extortionists, or abusive spam contacts to WhatsApp.",
            descriptionBn = "হোয়াটসঅ্যাপে প্রতারণা, চাঁদাবাজি বা ক্ষতিকর নম্বর রিপোর্ট ও ব্লক করার প্রক্রিয়া।",
            stepsEn = listOf(
                "Open the chat with suspicious number",
                "Tap contact info at top",
                "Scroll down to 'Report Contact'",
                "Keep 'Block contact and delete chat messages' unchecked if preserving evidence",
                "Send report to WhatsApp review team"
            ),
            stepsBn = listOf(
                "সন্দেহভাজন নম্বরের চ্যাট খুলুন",
                "নামের উপর ট্যাপ করে নিচে 'Report Contact' নির্বাচন করুন",
                "প্রমাণ প্রয়োজন হলে চ্যাট ব্যাকআপ নিশ্চিত করুন"
            )
        ),
        PlatformInfo(
            name = "YouTube",
            iconEmoji = "▶️",
            reportUrl = "https://support.google.com/youtube/answer/2802027",
            helpCenterUrl = "https://support.google.com/youtube/",
            descriptionEn = "Google YouTube policy enforcement, copyright takedown & defamation reporting.",
            descriptionBn = "ইউটিউবে আপত্তিকর বা মানহানিকর ভিডিও অপসারণ ও কপিরাইট স্ট্রাইকের অফিশিয়াল আবেদন।",
            stepsEn = listOf(
                "Click the 3 dots under video or channel About tab",
                "Click 'Report'",
                "Select category (Harassment, Hateful content, Privacy)",
                "Provide exact timestamp where violation occurs"
            ),
            stepsBn = listOf(
                "ভিডিওর নিচে ৩টি ডটে ক্লিক করে 'Report' নির্বাচন করুন",
                "সুনির্দিষ্ট নীতি লঙ্ঘন চিহ্নিত করে টাইমস্ট্যাম্প উল্লেখ করুন"
            )
        ),
        PlatformInfo(
            name = "TikTok",
            iconEmoji = "🎵",
            reportUrl = "https://support.tiktok.com/en/safety-hc/account-and-user-safety/report-a-problem",
            helpCenterUrl = "https://support.tiktok.com/",
            descriptionEn = "TikTok safety center for impersonation, harmful content and bullying.",
            descriptionBn = "টিকটকে ভুয়া একাউন্ট বা বুলিং কনটেন্ট রিপোর্ট করার অফিশিয়াল সাহায্য।",
            stepsEn = listOf(
                "Tap 'Share' arrow on video or 3 dots on profile",
                "Tap 'Report'",
                "Select applicable violation guideline"
            ),
            stepsBn = listOf(
                "প্রোফাইল বা ভিডিওর শেয়ার অপশন থেকে 'Report' ট্যাপ করুন",
                "সঠিক নীতিমালা লঙ্ঘন নির্বাচন করুন"
            )
        ),
        PlatformInfo(
            name = "X (Twitter)",
            iconEmoji = "🐦",
            reportUrl = "https://help.x.com/en/safety-and-security/report-abusive-behavior",
            helpCenterUrl = "https://help.x.com/",
            descriptionEn = "Report hateful conduct, non-consensual media, or impersonation on X.",
            descriptionBn = "এক্স (টুইটার)-এ ভুয়া অ্যাকাউন্ট, অপপ্রচার ও হয়রানি রিপোর্টের লিংক।",
            stepsEn = listOf(
                "Click the 3 dots menu on the Post or Profile",
                "Select 'Report Post' or 'Report @handle'",
                "Specify who is being targeted and submit"
            ),
            stepsBn = listOf(
                "পোস্ট বা প্রোফাইলের মেনু থেকে 'Report' নির্বাচন করুন",
                "ভুক্তভোগীর তথ্য ও লঙ্ঘনের বিবরণ দিন"
            )
        ),
        PlatformInfo(
            name = "Bangladesh Cyber Police / CID",
            iconEmoji = "🇧🇩",
            reportUrl = "https://police.gov.bd",
            helpCenterUrl = "https://cid.police.gov.bd",
            descriptionEn = "National Cyber Crime Investigation Division (CID), Helpline: 13219, National Emergency: 999.",
            descriptionBn = "বাংলাদেশ পুলিশ সাইবার পুলিশ সেন্টার (CID), হেল্পলাইন: 01769691522 / 13219, জাতীয় জরুরি সেবা: 999।",
            stepsEn = listOf(
                "Preserve all unedited screenshots and Case PDF from ICS Pro",
                "Call National Cyber Crime Helpline: 13219 or CID Cyber Police: 01769691522",
                "File a General Diary (GD) or FIR at your nearest Police Station",
                "Submit ICS Case PDF as supporting legal evidence document"
            ),
            stepsBn = listOf(
                "আইসিএস প্রো থেকে পূর্ণাঙ্গ পিডিএফ কেস রিপোর্ট ও প্রমাণাদি প্রস্তুত রাখুন",
                "পুলিশ সাইবার সাপোর্ট ফর উইমেন / সিআইডি হেল্পলাইন: ০১৭৬৯-৬৯১৫২২ বা ১৩২১৯-এ কল করুন",
                "নিকটস্থ থানায় আইসিএস রিপোর্টের প্রিন্ট কপি সহ জিডি (GD) করুন"
            )
        ),
        PlatformInfo(
            name = "Other / General",
            iconEmoji = "🌐",
            reportUrl = "https://www.interpol.int/en/Crimes/Cybercrime",
            helpCenterUrl = "https://www.cybercrime.gov.in",
            descriptionEn = "General international cyber incident documentation & law enforcement escalation.",
            descriptionBn = "সাধারণ আন্তর্জাতিক সাইবার অপরাধ ডকুমেন্টেশন ও আইনি সহায়তা কেন্দ্র।",
            stepsEn = listOf(
                "Export complete case package from ICS Pro",
                "Forward report to platform Abuse Desk or your local cyber cell"
            ),
            stepsBn = listOf(
                "আইসিএস প্রো থেকে কেস রিপোর্ট এক্সপোর্ট করে সংশ্লিষ্ট দপ্তরে পাঠান"
            )
        )
    )
}
