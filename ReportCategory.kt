package com.example.model

data class CategoryItem(
    val id: String,
    val icon: String,
    val nameEn: String,
    val nameBn: String,
    val severityDefault: String, // Critical, High, Medium, Low
    val guidanceEn: String,
    val guidanceBn: String
)

object ReportCategoryRegistry {
    val categories = listOf(
        CategoryItem(
            id = "impersonation",
            icon = "🚫",
            nameEn = "Impersonation / Fake Identity",
            nameBn = "ছদ্মবেশ / ফেক পরিচয়",
            severityDefault = "High",
            guidanceEn = "Document target profile URL, impersonated person's official identity, and comparative screenshots of bio and avatar.",
            guidanceBn = "আসল ব্যক্তির এনআইডি/প্রোফাইল এবং ভুয়া অ্যাকাউন্টের তুলনামূলক স্ক্রিনশট ও ইউআরএল সংরক্ষণ করুন।"
        ),
        CategoryItem(
            id = "harassment",
            icon = "😡",
            nameEn = "Harassment / Bullying",
            nameBn = "হয়রানি / সাইবার বুলিং",
            severityDefault = "High",
            guidanceEn = "Keep full message context, timestamps, sender handles, and unedited chat logs before blocking.",
            guidanceBn = "ব্লক করার আগেই পুরো কথোপকথন, তারিখ, সময় ও প্রেরকের আইডি সহ আন-এডিটেড স্ক্রিনশট রাখুন।"
        ),
        CategoryItem(
            id = "scam",
            icon = "💰",
            nameEn = "Scam / Fraud",
            nameBn = "প্রতারণা / স্ক্যাম",
            severityDefault = "High",
            guidanceEn = "Capture transaction references, payment numbers (bKash/Nagad/Bank), conversation promises, and fake voucher copies.",
            guidanceBn = "লেনদেন নম্বর (বিকাশ/নগদ/ব্যাংক), ট্রানজেকশন আইডি এবং প্রতারকের কথোপকথনের স্ক্রিনশট নিন।"
        ),
        CategoryItem(
            id = "hacked",
            icon = "🔓",
            nameEn = "Hacked / Compromised Account",
            nameBn = "হ্যাকড / বেদখল অ্যাকাউন্ট",
            severityDefault = "Critical",
            guidanceEn = "Save recovery alert emails, changed recovery phone/email notices, and suspicious login location logs.",
            guidanceBn = "অ্যাকাউন্টে ঢোকার নোটিফিকেশন, সিকিউরিটি ইমেইল ও পাসওয়ার্ড পরিবর্তনের তারিখ সংরক্ষণ করুন।"
        ),
        CategoryItem(
            id = "sexual_nudity",
            icon = "🔞",
            nameEn = "Sexual / Nudity / 18+ Content",
            nameBn = "যৌন / ১৮+ আপত্তিকর কনটেন্ট",
            severityDefault = "Critical",
            guidanceEn = "Note post/story URL immediately and capture visible violations in compliance with legal evidence rules.",
            guidanceBn = "কনটেন্টের পোস্ট ইউআরএল এবং প্রকাশের তারিখ-সময় দ্রুত সংরক্ষণ করুন।"
        ),
        CategoryItem(
            id = "spam",
            icon = "📨",
            nameEn = "Spam",
            nameBn = "স্প্যাম ও বিভ্রান্তিকর বার্তা",
            severityDefault = "Low",
            guidanceEn = "Collect unsolicited mass messaging links, bot account lists, and frequency of communication.",
            guidanceBn = "অযাচিত বার্তার লিংক, প্রেরকের তালিকা ও স্প্যামিংয়ের সময়ের রেকর্ড রাখুন।"
        ),
        CategoryItem(
            id = "threat_violence",
            icon = "⚠️",
            nameEn = "Threat / Violence",
            nameBn = "হুমকি / সহিংসতা",
            severityDefault = "Critical",
            guidanceEn = "Preserve explicit death/harm threats with full header info and consider immediate law enforcement notification.",
            guidanceBn = "জীবননাশের হুমকি বা শারীরিক ক্ষতির স্পষ্ট প্রমাণ রাখুন এবং দ্রুত নিকটস্থ থানায় জিডি করুন।"
        ),
        CategoryItem(
            id = "privacy_violation",
            icon = "🕵️",
            nameEn = "Privacy Violation",
            nameBn = "ব্যক্তিগত গোপনীয়তা লঙ্ঘন",
            severityDefault = "High",
            guidanceEn = "Save unauthorized disclosure of phone number, home address, NID, medical or financial confidential data.",
            guidanceBn = "ফোন নম্বর, ঠিকানা, এনআইডি বা ব্যক্তিগত তথ্যের অননুমোদিত প্রকাশের লিঙ্ক ও স্ক্রিনশট রাখুন।"
        ),
        CategoryItem(
            id = "non_consensual_media",
            icon = "📷",
            nameEn = "Non-consensual Image/Video",
            nameBn = "সম্মতিহীন ছবি বা ভিডিও ছড়ানো",
            severityDefault = "Critical",
            guidanceEn = "Urgent: record distributing URLs, groups, telegram channels, or cloud hosting links without viewing unauthorized content.",
            guidanceBn = "যেসব পেজ, গ্রুপ বা ক্লাউড স্টোরেজে ছড়ানো হচ্ছে তার লিংক এবং সংশ্লিষ্ট পোস্ট দ্রুত নোট করুন।"
        ),
        CategoryItem(
            id = "fake_profile",
            icon = "👤",
            nameEn = "Fake Profile / Fake Page",
            nameBn = "ভুয়া প্রোফাইল / ভুয়া পেজ",
            severityDefault = "Medium",
            guidanceEn = "Note creation date, numerical profile ID (UID), duplicated posts, and fraudulent intent indicators.",
            guidanceBn = "আইডির ইউজারনেম, প্রোফাইল ইউআইডি এবং কপি করা পোস্টগুলোর প্রমাণ সংগ্রহ করুন।"
        ),
        CategoryItem(
            id = "copyright_ip",
            icon = "©️",
            nameEn = "Copyright / Intellectual Property",
            nameBn = "কপিরাইট / মেধাস্বত্ব লঙ্ঘন",
            severityDefault = "Medium",
            guidanceEn = "Prepare proof of original ownership (registration, original publication date, timestamped source).",
            guidanceBn = "আপনার মূল কন্টেন্টের কপিরাইট প্রমাণ বা প্রথম প্রকাশের তারিখ ও লিংক প্রস্তুত রাখুন।"
        ),
        CategoryItem(
            id = "trademark_brand",
            icon = "™️",
            nameEn = "Trademark / Brand Impersonation",
            nameBn = "ট্রেডমার্ক / ব্র্যান্ড নকল",
            severityDefault = "High",
            guidanceEn = "Provide trademark registration docs, official brand URL, and proof of consumer confusion or sales fraud.",
            guidanceBn = "অফিসিয়াল ট্রেডমার্ক সনদ এবং ভুয়া ব্র্যান্ড দ্বারা গ্রাহকদের বিভ্রান্ত করার প্রমাণ দিন।"
        ),
        CategoryItem(
            id = "phishing",
            icon = "🎣",
            nameEn = "Phishing / Malicious Link",
            nameBn = "ফিশিং / ক্ষতিকর লিংক",
            severityDefault = "Critical",
            guidanceEn = "Copy full malicious URL without visiting, capture SMS/email sender header, and domain spoofing details.",
            guidanceBn = "ফিশিং ডোমেইনের পূর্ণ লিংক (যেমন: fake-facebook.xyz) এবং এসএমএস/ইমেইল হেডার সংরক্ষণ করুন।"
        ),
        CategoryItem(
            id = "financial_scam",
            icon = "💳",
            nameEn = "Financial Scam",
            nameBn = "আর্থিক প্রতারণা ও ডিজিটাল জালিয়াতি",
            severityDefault = "Critical",
            guidanceEn = "Keep banking statements, transaction IDs, mobile money wallet statements, and communication logs.",
            guidanceBn = "ব্যাংক স্টেটমেন্ট, এমএফএস ট্রানজেকশন স্টেটমেন্ট ও অর্থ পাঠানোর প্রমাণের কপি রাখুন।"
        ),
        CategoryItem(
            id = "misinformation",
            icon = "📰",
            nameEn = "False / Misleading Information",
            nameBn = "গুজব / বিভ্রান্তিকর তথ্য",
            severityDefault = "Medium",
            guidanceEn = "Gather fact-checking source comparisons, manipulated video/audio source, and viral propagation evidence.",
            guidanceBn = "ফ্যাক্ট-চেক তথ্যসূত্র এবং এডিটেড ছবি/ভিডিওর আসল উৎস তুলনামূলকভাবে সংরক্ষণ করুন।"
        ),
        CategoryItem(
            id = "child_safety",
            icon = "🧒",
            nameEn = "Child Safety Concern",
            nameBn = "শিশু নিরাপত্তা বিষয়ক",
            severityDefault = "Critical",
            guidanceEn = "Critical priority. Immediately preserve platform links and alert emergency police cyber child protection desk.",
            guidanceBn = "সর্বোচ্চ জরুরি। অবিলম্বে লিংক ও তথ্য সংগ্রহ করে পুলিশের সাইবার হেল্পলাইনে যোগাযোগ করুন।"
        ),
        CategoryItem(
            id = "extortion_blackmail",
            icon = "🛑",
            nameEn = "Extortion / Blackmail",
            nameBn = "চাঁদাবাজি / ব্ল্যাকমেইল",
            severityDefault = "Critical",
            guidanceEn = "Never delete chats. Record extortion demands, payment deadlines, threatening voice notes, and target URLs.",
            guidanceBn = "কোনো বার্তা মুছবেন না। চাঁদার দাবি, ভয়েস রেকর্ড, হুমকি ও সময়সীমার স্পষ্ট প্রমাণ রাখুন।"
        ),
        CategoryItem(
            id = "abusive_hateful",
            icon = "📢",
            nameEn = "Abusive / Hateful Content",
            nameBn = "ঘৃণাসূচক / আপত্তিকর বক্তব্য",
            severityDefault = "High",
            guidanceEn = "Capture target community attacks, religious/ethnic hate slurs, and incitement to violence.",
            guidanceBn = "উস্কানিমূলক বা বিদ্বেষমূলক বক্তব্যের পোস্টের লিংক এবং স্ক্রিনশট প্রমাণ রাখুন।"
        ),
        CategoryItem(
            id = "malicious_site",
            icon = "🔗",
            nameEn = "Malicious Website / Link",
            nameBn = "ক্ষতিকর ওয়েবসাইট বা ম্যালওয়্যার লিংক",
            severityDefault = "High",
            guidanceEn = "Document domain, IP if known, malware download prompts, and deceptive landing pages.",
            guidanceBn = "ম্যালওয়্যার ছড়ানোর লিংক ও ভুয়া ওয়েবসাইটের পূর্ণ বিবরণ নোট করুন।"
        ),
        CategoryItem(
            id = "other",
            icon = "❓",
            nameEn = "Other",
            nameBn = "অন্যান্য সাইবার অপরাধ",
            severityDefault = "Medium",
            guidanceEn = "Detail any cyber incident not listed above with relevant technical and circumstantial evidence.",
            guidanceBn = "উপরে উল্লেখিত ছাড়া অন্য যেকোনো সাইবার সমস্যার বিস্তারিত তথ্য ও প্রমাণ লিপিবদ্ধ করুন।"
        )
    )

    fun find(id: String): CategoryItem =
        categories.firstOrNull { it.id.equals(id, ignoreCase = true) || it.nameEn.equals(id, ignoreCase = true) }
            ?: categories.last()
}
