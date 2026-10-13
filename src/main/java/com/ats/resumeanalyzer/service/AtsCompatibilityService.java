package com.ats.resumeanalyzer.service;

import com.ats.resumeanalyzer.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Evaluates resume text against rule-based ATS formatting benchmarks,
 * providing a transparent checklist and compatibility score out of 100.
 */
@Service
public class AtsCompatibilityService {

    /**
     * Conducts standard ATS checks on extracted resume data.
     *
     * @param data the extracted PDF data
     * @return List of AtsCheckItem representing individual rule results
     */
    public List<AtsCheckItem> evaluateAtsCompatibility(ExtractedPdfData data) {
        List<AtsCheckItem> checklist = new ArrayList<>();
        Map<String, String> sections = data.getSectionTexts();
        ContactInfo contact = data.getContactInfo();
        String rawText = data.getRawText();
        int wordCount = data.getWordCount();

        // 1. Text Extractability Check (Weight: 15)
        if (data.isLikelyScanned() || wordCount < 40) {
            checklist.add(new AtsCheckItem(
                    "ATS-01",
                    "Text Extractability & Machine Readability",
                    "File Parsing",
                    AtsCheckStatus.FAILED,
                    15,
                    0,
                    "Text could not be cleanly extracted. Document appears to be a scanned image or non-OCR PDF.",
                    "Ensure your resume is exported directly from Microsoft Word, Google Docs, or LaTeX as a text-based PDF rather than scanned as an image."
            ));
        } else if (wordCount < 100) {
            checklist.add(new AtsCheckItem(
                    "ATS-01",
                    "Text Extractability & Machine Readability",
                    "File Parsing",
                    AtsCheckStatus.WARNING,
                    15,
                    8,
                    "Extracted text volume is very low (" + wordCount + " words). Parts of the resume might be embedded inside images or graphics.",
                    "Verify that text is selectable and copyable in your PDF reader. Avoid embedding text inside graphic elements or images."
            ));
        } else {
            checklist.add(new AtsCheckItem(
                    "ATS-01",
                    "Text Extractability & Machine Readability",
                    "File Parsing",
                    AtsCheckStatus.PASSED,
                    15,
                    15,
                    "Text extracted successfully with " + wordCount + " words across " + data.getPageCount() + " page(s).",
                    "Machine parsers can read text cleanly in natural reading order."
            ));
        }

        // 2. Email Address Detection (Weight: 10)
        if (contact != null && contact.isEmailFound()) {
            checklist.add(new AtsCheckItem(
                    "ATS-02",
                    "Contact Information: Email Address",
                    "Contact Details",
                    AtsCheckStatus.PASSED,
                    10,
                    10,
                    "Detected professional email address: " + contact.getEmail(),
                    "Recruiters and automated candidate workflows can contact you directly."
            ));
        } else {
            checklist.add(new AtsCheckItem(
                    "ATS-02",
                    "Contact Information: Email Address",
                    "Contact Details",
                    AtsCheckStatus.FAILED,
                    10,
                    0,
                    "No valid email address was detected in the parsed text.",
                    "Add your email address clearly in the header section. Avoid placing contact details inside embedded image headers."
            ));
        }

        // 3. Phone Number Detection (Weight: 10)
        if (contact != null && contact.isPhoneFound()) {
            checklist.add(new AtsCheckItem(
                    "ATS-03",
                    "Contact Information: Phone Number",
                    "Contact Details",
                    AtsCheckStatus.PASSED,
                    10,
                    10,
                    "Detected candidate phone number: " + contact.getPhone(),
                    "Standard phone format detected."
            ));
        } else {
            checklist.add(new AtsCheckItem(
                    "ATS-03",
                    "Contact Information: Phone Number",
                    "Contact Details",
                    AtsCheckStatus.WARNING,
                    10,
                    3,
                    "Phone number was not detected in standard 10-14 digit formats.",
                    "Include a clean telephone number with country code (e.g. +91 98765 43210 or (555) 123-4567)."
            ));
        }

        // 4. Online Professional Presence (LinkedIn / GitHub / Portfolio) (Weight: 5)
        if (contact != null && contact.isLinksFound()) {
            List<String> foundLinks = new ArrayList<>();
            if (contact.getLinkedInUrl() != null) foundLinks.add("LinkedIn");
            if (contact.getGitHubUrl() != null) foundLinks.add("GitHub");
            if (contact.getPortfolioUrl() != null) foundLinks.add("Portfolio");

            checklist.add(new AtsCheckItem(
                    "ATS-04",
                    "Professional Online Links (LinkedIn / GitHub)",
                    "Contact Details",
                    AtsCheckStatus.PASSED,
                    5,
                    5,
                    "Detected link(s): " + String.join(", ", foundLinks),
                    "Including GitHub and LinkedIn links significantly improves technical evaluation chances."
            ));
        } else {
            checklist.add(new AtsCheckItem(
                    "ATS-04",
                    "Professional Online Links (LinkedIn / GitHub)",
                    "Contact Details",
                    AtsCheckStatus.WARNING,
                    5,
                    2,
                    "No LinkedIn profile or GitHub repository links were detected.",
                    "Add clickable links to your LinkedIn profile and GitHub portfolio to demonstrate active coding."
            ));
        }

        // 5. Standard Education Section (Weight: 15)
        boolean hasEducationSection = sections.containsKey("Education");
        boolean hasEducationContent = hasEducationSection && (sections.get("Education").length() > 20);

        if (hasEducationContent) {
            checklist.add(new AtsCheckItem(
                    "ATS-05",
                    "Standard Section: Education",
                    "Section Headings",
                    AtsCheckStatus.PASSED,
                    15,
                    15,
                    "Education section detected with standard heading and degree/institution details.",
                    "Standard section names help ATS categorize academic degree qualifications accurately."
            ));
        } else if (hasEducationSection) {
            checklist.add(new AtsCheckItem(
                    "ATS-05",
                    "Standard Section: Education",
                    "Section Headings",
                    AtsCheckStatus.WARNING,
                    15,
                    8,
                    "Education heading detected, but section content is brief or lacking university/degree details.",
                    "Include degree name (e.g. B.Tech / B.E. / B.S.), major (Computer Science), university name, and graduation year."
            ));
        } else {
            // Check if education keywords appear anywhere in text
            boolean looseEdu = rawText.toLowerCase().matches(".*\\b(b\\.tech|b\\.e\\.|bachelor|degree|university|college|cgpa|gpa)\\b.*");
            checklist.add(new AtsCheckItem(
                    "ATS-05",
                    "Standard Section: Education",
                    "Section Headings",
                    looseEdu ? AtsCheckStatus.WARNING : AtsCheckStatus.FAILED,
                    15,
                    looseEdu ? 6 : 0,
                    looseEdu ? "Academic keywords found in resume, but missing a dedicated 'Education' section heading."
                            : "No standard 'Education' section detected.",
                    "Create an explicit section with the heading 'Education' to ensure automated resume parsers parse your degree."
            ));
        }

        // 6. Standard Skills Section (Weight: 15)
        boolean hasSkillsSection = sections.containsKey("Skills");
        if (hasSkillsSection && sections.get("Skills").length() > 25) {
            checklist.add(new AtsCheckItem(
                    "ATS-06",
                    "Standard Section: Technical Skills",
                    "Section Headings",
                    AtsCheckStatus.PASSED,
                    15,
                    15,
                    "Distinct 'Skills' section detected with organized technical competencies.",
                    "ATS parsers prioritize skills grouped under explicit 'Skills' or 'Technical Skills' headers."
            ));
        } else if (hasSkillsSection) {
            checklist.add(new AtsCheckItem(
                    "ATS-06",
                    "Standard Section: Technical Skills",
                    "Section Headings",
                    AtsCheckStatus.WARNING,
                    15,
                    8,
                    "Skills header found, but the section contains few items or lacks clear categorization.",
                    "Organize your skills clearly (e.g., Languages: Java, Python; Frameworks: Spring Boot; Databases: SQL)."
            ));
        } else {
            checklist.add(new AtsCheckItem(
                    "ATS-06",
                    "Standard Section: Technical Skills",
                    "Section Headings",
                    AtsCheckStatus.WARNING,
                    15,
                    5,
                    "No dedicated 'Skills' heading detected, though individual skill keywords may exist across the resume.",
                    "Always create a designated section titled 'Technical Skills' near the top of your resume."
            ));
        }

        // 7. Projects & Experience Sections (Weight: 15)
        boolean hasProjects = sections.containsKey("Projects");
        boolean hasExperience = sections.containsKey("Experience");

        if (hasProjects && hasExperience) {
            checklist.add(new AtsCheckItem(
                    "ATS-07",
                    "Evidence of Work Experience & Projects",
                    "Content Structure",
                    AtsCheckStatus.PASSED,
                    15,
                    15,
                    "Both 'Projects' and 'Work Experience / Internships' sections detected.",
                    "Provides excellent balance of practical project engineering and professional experience."
            ));
        } else if (hasProjects || hasExperience) {
            String detectedName = hasProjects ? "Projects" : "Work Experience";
            checklist.add(new AtsCheckItem(
                    "ATS-07",
                    "Evidence of Work Experience & Projects",
                    "Content Structure",
                    AtsCheckStatus.PASSED,
                    15,
                    13,
                    "Dedicated '" + detectedName + "' section detected.",
                    "Students without formal corporate experience should feature 2 to 3 substantive technical projects."
            ));
        } else {
            checklist.add(new AtsCheckItem(
                    "ATS-07",
                    "Evidence of Work Experience & Projects",
                    "Content Structure",
                    AtsCheckStatus.FAILED,
                    15,
                    0,
                    "Neither a 'Projects' nor a 'Work Experience' section heading was detected.",
                    "Add a prominent 'Projects' section describing 2-3 real applications with technologies used and outcomes."
            ));
        }

        // 8. Word Count & Length Appropriateness (Weight: 10)
        if (wordCount >= 250 && wordCount <= 1000) {
            checklist.add(new AtsCheckItem(
                    "ATS-08",
                    "Resume Word Count & Volume",
                    "Formatting & Length",
                    AtsCheckStatus.PASSED,
                    10,
                    10,
                    "Resume length is optimal: " + wordCount + " words (ideal 1 to 2 page density).",
                    "Word count is well-balanced for automated screening and human recruiters."
            ));
        } else if (wordCount > 1000 && wordCount <= 1400) {
            checklist.add(new AtsCheckItem(
                    "ATS-08",
                    "Resume Word Count & Volume",
                    "Formatting & Length",
                    AtsCheckStatus.WARNING,
                    10,
                    7,
                    "Resume length is relatively long (" + wordCount + " words). Entry-level resumes should ideally remain 1 page (400-700 words).",
                    "Be concise: condense older secondary details and focus on recent impactful technical projects."
            ));
        } else if (wordCount < 250 && !data.isLikelyScanned()) {
            checklist.add(new AtsCheckItem(
                    "ATS-08",
                    "Resume Word Count & Volume",
                    "Formatting & Length",
                    AtsCheckStatus.WARNING,
                    10,
                    4,
                    "Resume content appears brief (" + wordCount + " words). Important details may be missing.",
                    "Expand on project descriptions, responsibilities, tools utilized, and measurable achievements."
            ));
        } else {
            checklist.add(new AtsCheckItem(
                    "ATS-08",
                    "Resume Word Count & Volume",
                    "Formatting & Length",
                    AtsCheckStatus.FAILED,
                    10,
                    2,
                    "Unusual word count (" + wordCount + " words). Resume may be empty or excessively verbose.",
                    "Aim for 350 to 800 words for an internship or entry-level software resume."
            ));
        }

        // 9. Formatting Cleanliness & Text Order (Weight: 5)
        long nonAsciiCount = rawText.chars().filter(ch -> ch > 127).count();
        double nonAsciiRatio = rawText.length() > 0 ? (double) nonAsciiCount / rawText.length() : 0.0;

        if (nonAsciiRatio < 0.05) {
            checklist.add(new AtsCheckItem(
                    "ATS-09",
                    "Text Order & Character Cleanliness",
                    "Parsing Quality",
                    AtsCheckStatus.PASSED,
                    5,
                    5,
                    "Clean text flow. Minimal non-standard characters or parsing distortions.",
                    "Standard fonts (Arial, Calibri, Times New Roman, Roboto) ensure highest parser accuracy."
            ));
        } else {
            checklist.add(new AtsCheckItem(
                    "ATS-09",
                    "Text Order & Character Cleanliness",
                    "Parsing Quality",
                    AtsCheckStatus.WARNING,
                    5,
                    2,
                    "High ratio of non-standard characters detected (" + String.format("%.1f%%", nonAsciiRatio * 100) + "). Special bullets or custom fonts may be causing artifacts.",
                    "Replace custom icon bullets, fancy symbols, or decorative typography with standard round bullets (- or *)."
            ));
        }

        return checklist;
    }

    /**
     * Calculates the aggregate ATS compatibility score based on checklist weights and earned points.
     */
    public int calculateAtsScore(List<AtsCheckItem> checklist) {
        if (checklist == null || checklist.isEmpty()) {
            return 0;
        }

        int totalEarned = 0;
        for (AtsCheckItem item : checklist) {
            totalEarned += item.getScoreEarned();
        }

        return Math.min(100, Math.max(0, totalEarned));
    }
}
