package com.ats.resumeanalyzer.service;

import com.ats.resumeanalyzer.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to generate downloadable, professional analysis reports in PDF and text formats.
 */
@Service
public class AnalysisReportService {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisReportService.class);

    /**
     * Generates a multi-page, formatted PDF analysis report using Apache PDFBox.
     *
     * @param result the complete resume analysis result
     * @return byte array of the generated PDF document
     */
    public byte[] generatePdfReport(ResumeAnalysisResult result) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            float margin = 40;
            float yStart = page.getMediaBox().getHeight() - margin;
            float yPosition = yStart;
            float leading = 14;

            PDPageContentStream cs = new PDPageContentStream(doc, page);

            // Title Header
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("ATS Resume Analysis & Career Guidance Report");
            cs.endText();
            yPosition -= 24;

            // Subtitle & Metadata
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 10);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("Generated: " + result.getAnalysisTimestamp() + " | File: " + result.getFileName());
            cs.endText();
            yPosition -= 16;

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 10);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("Target Job Role: " + result.getTargetJobRole() + " | Candidate: " + result.getContactInfo().getCandidateName());
            cs.endText();
            yPosition -= 25;

            // Score Summary Box
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("Executive Scores:");
            cs.endText();
            yPosition -= 18;

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
            cs.newLineAtOffset(margin + 15, yPosition);
            cs.showText(String.format("1. ATS Compatibility Score: %d / 100", result.getScores().getAtsCompatibilityTotal()));
            cs.endText();
            yPosition -= 16;

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
            cs.newLineAtOffset(margin + 15, yPosition);
            cs.showText(String.format("2. Role Suitability Score: %d / 100", result.getScores().getRoleSuitabilityTotal()));
            cs.endText();
            yPosition -= 16;

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA, 9);
            cs.newLineAtOffset(margin + 25, yPosition);
            cs.showText(String.format("- Skills Match: %d/50 | Projects & Experience: %d/30 | Qualifications: %d/20",
                    result.getScores().getSkillsScore(),
                    result.getScores().getProjectsAndExperienceScore(),
                    result.getScores().getQualificationsAndCertsScore()));
            cs.endText();
            yPosition -= 22;

            // Section: Skills Overview
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("Skills Assessment:");
            cs.endText();
            yPosition -= 16;

            String matchedStr = result.getMatchedSkills().isEmpty() ? "None detected" : String.join(", ", result.getMatchedSkills());
            yPosition = drawWrappedText(cs, doc, margin + 10, yPosition, "Matched Skills: " + matchedStr, 9, false);

            String missingStr = result.getMissingSkills().isEmpty() ? "None" : String.join(", ", result.getMissingSkills());
            yPosition = drawWrappedText(cs, doc, margin + 10, yPosition, "Skills Not Detected in Resume: " + missingStr, 9, false);
            yPosition -= 10;

            // Section: ATS Checklist Highlights
            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("ATS Compatibility Checklist:");
            cs.endText();
            yPosition -= 16;

            for (AtsCheckItem item : result.getAtsChecklist()) {
                String statusStr = "[" + item.getStatus().name() + "]";
                String line = String.format("%s %s (%d/%d pts): %s",
                        statusStr, item.getRuleName(), item.getScoreEarned(), item.getWeight(), item.getDetails());
                yPosition = drawWrappedText(cs, doc, margin + 10, yPosition, line, 8, false);
                if (yPosition < 70) {
                    cs.close();
                    page = new PDPage(PDRectangle.A4);
                    doc.addPage(page);
                    cs = new PDPageContentStream(doc, page);
                    yPosition = yStart;
                }
            }
            yPosition -= 10;

            // Section: Project Recommendations
            if (yPosition < 150) {
                cs.close();
                page = new PDPage(PDRectangle.A4);
                doc.addPage(page);
                cs = new PDPageContentStream(doc, page);
                yPosition = yStart;
            }

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("Recommended Projects to Build:");
            cs.endText();
            yPosition -= 16;

            for (ProjectRecommendation proj : result.getRecommendations().getRecommendedProjects()) {
                String projLine = "* " + proj.getTitle() + " (" + proj.getComplexity() + "): " + proj.getDescription();
                yPosition = drawWrappedText(cs, doc, margin + 10, yPosition, projLine, 8, false);
                String stack = "  Tech Stack: " + String.join(", ", proj.getKeyTechnologies());
                yPosition = drawWrappedText(cs, doc, margin + 15, yPosition, stack, 8, false);
                if (yPosition < 70) {
                    cs.close();
                    page = new PDPage(PDRectangle.A4);
                    doc.addPage(page);
                    cs = new PDPageContentStream(doc, page);
                    yPosition = yStart;
                }
            }
            yPosition -= 10;

            // Section: Certifications
            if (yPosition < 140) {
                cs.close();
                page = new PDPage(PDRectangle.A4);
                doc.addPage(page);
                cs = new PDPageContentStream(doc, page);
                yPosition = yStart;
            }

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("Recommended Certifications & Courses:");
            cs.endText();
            yPosition -= 16;

            for (CertificationItem cert : result.getRecommendations().getRecommendedCertifications()) {
                String certLine = "* " + cert.getTitle() + " | Provider: " + cert.getProvider()
                        + " [" + cert.getCredentialType() + " - " + cert.getCostCategory() + "]";
                yPosition = drawWrappedText(cs, doc, margin + 10, yPosition, certLine, 8, false);
                yPosition = drawWrappedText(cs, doc, margin + 15, yPosition, "  Link: " + cert.getOfficialUrl(), 8, false);
                if (yPosition < 70) {
                    cs.close();
                    page = new PDPage(PDRectangle.A4);
                    doc.addPage(page);
                    cs = new PDPageContentStream(doc, page);
                    yPosition = yStart;
                }
            }
            yPosition -= 10;

            // Section: Resume Improvement Suggestions
            if (yPosition < 120) {
                cs.close();
                page = new PDPage(PDRectangle.A4);
                doc.addPage(page);
                cs = new PDPageContentStream(doc, page);
                yPosition = yStart;
            }

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText("Actionable Resume Writing Tips:");
            cs.endText();
            yPosition -= 16;

            for (String tip : result.getRecommendations().getResumeImprovementSuggestions()) {
                yPosition = drawWrappedText(cs, doc, margin + 10, yPosition, "- " + tip, 8, false);
                if (yPosition < 70) {
                    cs.close();
                    page = new PDPage(PDRectangle.A4);
                    doc.addPage(page);
                    cs = new PDPageContentStream(doc, page);
                    yPosition = yStart;
                }
            }

            // Disclaimer Footer
            yPosition -= 15;
            if (yPosition < 50) {
                cs.close();
                page = new PDPage(PDRectangle.A4);
                doc.addPage(page);
                cs = new PDPageContentStream(doc, page);
                yPosition = yStart;
            }

            cs.beginText();
            cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 7);
            cs.newLineAtOffset(margin, yPosition);
            cs.showText(result.getDisclaimer());
            cs.endText();

            cs.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            return baos.toByteArray();
        }
    }

    /**
     * Helper to render text with clean wrapping across lines.
     */
    private float drawWrappedText(PDPageContentStream cs, PDDocument doc, float x, float y, String text, int fontSize, boolean bold) throws IOException {
        if (text == null) return y;
        
        // Sanitize for PDF Type 1 encoding (ISO-8859-1 safe)
        String safeText = text.replaceAll("[^\\x20-\\x7E]", " ");
        
        int maxCharsPerLine = 95;
        List<String> lines = wrapWords(safeText, maxCharsPerLine);

        for (String line : lines) {
            cs.beginText();
            cs.setFont(bold ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, fontSize);
            cs.newLineAtOffset(x, y);
            cs.showText(line);
            cs.endText();
            y -= (fontSize + 4);
        }

        return y;
    }

    private List<String> wrapWords(String text, int maxChars) {
        List<String> result = new ArrayList<>();
        String[] words = text.split("\\s+");
        StringBuilder current = new StringBuilder();

        for (String word : words) {
            if (current.length() + word.length() + 1 > maxChars) {
                if (current.length() > 0) {
                    result.add(current.toString());
                    current = new StringBuilder();
                }
            }
            if (current.length() > 0) current.append(" ");
            current.append(word);
        }

        if (current.length() > 0) {
            result.add(current.toString());
        }

        return result;
    }

    /**
     * Generates a plain-text version of the analysis report.
     */
    public String generateTextReport(ResumeAnalysisResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================\n");
        sb.append("   ATS RESUME ANALYSIS & CAREER GUIDANCE REPORT\n");
        sb.append("========================================================================\n\n");
        sb.append("Generated At:       ").append(result.getAnalysisTimestamp()).append("\n");
        sb.append("Resume File:        ").append(result.getFileName()).append("\n");
        sb.append("Target Job Role:    ").append(result.getTargetJobRole()).append("\n");
        sb.append("Candidate Name:     ").append(result.getContactInfo().getCandidateName()).append("\n");
        sb.append("Email:              ").append(result.getContactInfo().getEmail()).append("\n");
        sb.append("Phone:              ").append(result.getContactInfo().getPhone()).append("\n\n");

        sb.append("------------------------------------------------------------------------\n");
        sb.append("1. EXECUTIVE SCORES\n");
        sb.append("------------------------------------------------------------------------\n");
        sb.append("ATS Compatibility Score:  ").append(result.getScores().getAtsCompatibilityTotal()).append("/100\n");
        sb.append("Role Suitability Score:   ").append(result.getScores().getRoleSuitabilityTotal()).append("/100\n");
        sb.append("  - Skills Match:         ").append(result.getScores().getSkillsScore()).append("/50\n");
        sb.append("  - Projects/Experience:  ").append(result.getScores().getProjectsAndExperienceScore()).append("/30\n");
        sb.append("  - Qualifications/Certs: ").append(result.getScores().getQualificationsAndCertsScore()).append("/20\n\n");

        sb.append("------------------------------------------------------------------------\n");
        sb.append("2. SKILLS EVALUATION\n");
        sb.append("------------------------------------------------------------------------\n");
        sb.append("Matched Role Skills:      ").append(String.join(", ", result.getMatchedSkills())).append("\n");
        sb.append("Skills Not Detected:      ").append(String.join(", ", result.getMissingSkills())).append("\n\n");

        sb.append("------------------------------------------------------------------------\n");
        sb.append("3. ATS COMPATIBILITY CHECKLIST\n");
        sb.append("------------------------------------------------------------------------\n");
        for (AtsCheckItem item : result.getAtsChecklist()) {
            sb.append(String.format("[%s] %s (%d/%d pts)\n", item.getStatus(), item.getRuleName(), item.getScoreEarned(), item.getWeight()));
            sb.append("    Finding:        ").append(item.getDetails()).append("\n");
            sb.append("    Recommendation: ").append(item.getRecommendation()).append("\n\n");
        }

        sb.append("------------------------------------------------------------------------\n");
        sb.append("4. RECOMMENDED PROJECTS\n");
        sb.append("------------------------------------------------------------------------\n");
        for (ProjectRecommendation proj : result.getRecommendations().getRecommendedProjects()) {
            sb.append("* ").append(proj.getTitle()).append(" [").append(proj.getComplexity()).append("]\n");
            sb.append("  Description: ").append(proj.getDescription()).append("\n");
            sb.append("  Tech Stack:  ").append(String.join(", ", proj.getKeyTechnologies())).append("\n");
            sb.append("  Resume Tip:  ").append(proj.getSuggestedResumeBullet()).append("\n\n");
        }

        sb.append("------------------------------------------------------------------------\n");
        sb.append("5. RECOMMENDED CERTIFICATIONS & COURSES\n");
        sb.append("------------------------------------------------------------------------\n");
        for (CertificationItem cert : result.getRecommendations().getRecommendedCertifications()) {
            sb.append("* ").append(cert.getTitle()).append(" (Provider: ").append(cert.getProvider()).append(")\n");
            sb.append("  Category: ").append(cert.getCredentialType()).append(" | ").append(cert.getCostCategory()).append("\n");
            sb.append("  URL:      ").append(cert.getOfficialUrl()).append("\n\n");
        }

        sb.append("------------------------------------------------------------------------\n");
        sb.append("6. RESUME IMPROVEMENT RECOMMENDATIONS\n");
        sb.append("------------------------------------------------------------------------\n");
        for (String tip : result.getRecommendations().getResumeImprovementSuggestions()) {
            sb.append("- ").append(tip).append("\n");
        }

        sb.append("\n========================================================================\n");
        sb.append("DISCLAIMER: ").append(result.getDisclaimer()).append("\n");
        sb.append("========================================================================\n");

        return sb.toString();
    }
}
