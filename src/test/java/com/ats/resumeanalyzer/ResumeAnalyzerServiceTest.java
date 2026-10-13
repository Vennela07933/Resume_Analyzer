package com.ats.resumeanalyzer;

import com.ats.resumeanalyzer.model.*;
import com.ats.resumeanalyzer.service.*;
import com.ats.resumeanalyzer.util.SampleResumeGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResumeAnalyzerServiceTest {

    private static File sampleJavaResumeFile;
    private static File sampleJuniorResumeFile;

    private final PdfTextExtractionService extractionService = new PdfTextExtractionService();
    private final AtsCompatibilityService atsService = new AtsCompatibilityService();
    private final SkillMatchingService skillService = new SkillMatchingService();
    private final JobRoleService roleService = new JobRoleService();
    private final CareerRecommendationService recommendationService = new CareerRecommendationService();

    @BeforeAll
    static void setup() throws IOException {
        File dir = new File("src/main/resources/static/sample-resumes");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        sampleJavaResumeFile = new File(dir, "Sample_Java_Developer_Resume.pdf");
        SampleResumeGenerator.generateSampleJavaResume(sampleJavaResumeFile);

        sampleJuniorResumeFile = new File(dir, "Sample_Junior_Resume_With_Gaps.pdf");
        SampleResumeGenerator.generateSampleJuniorResume(sampleJuniorResumeFile);
    }

    @Test
    void testExtractionAndAtsEvaluationOnJavaResume() throws IOException {
        try (FileInputStream fis = new FileInputStream(sampleJavaResumeFile)) {
            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",
                    "Sample_Java_Developer_Resume.pdf",
                    "application/pdf",
                    fis
            );

            ExtractedPdfData data = extractionService.extractText(mockFile);
            assertNotNull(data);
            assertFalse(data.isLikelyScanned());
            assertTrue(data.getWordCount() > 150);

            // Contact Info
            ContactInfo contact = data.getContactInfo();
            assertNotNull(contact);
            assertEquals("rahul.sharma@example.com", contact.getEmail());
            assertTrue(contact.isPhoneFound());
            assertTrue(contact.isLinksFound());

            // Check sections
            assertTrue(data.getSectionTexts().containsKey("Education"));
            assertTrue(data.getSectionTexts().containsKey("Skills"));
            assertTrue(data.getSectionTexts().containsKey("Projects"));
            assertTrue(data.getSectionTexts().containsKey("Experience"));

            // ATS Evaluation
            List<AtsCheckItem> checklist = atsService.evaluateAtsCompatibility(data);
            assertFalse(checklist.isEmpty());
            int score = atsService.calculateAtsScore(checklist);
            assertTrue(score >= 85, "Expected high ATS score for well-formatted resume, got: " + score);
        }
    }

    @Test
    void testSkillMatchingForJavaDeveloper() throws IOException {
        roleService.initRoles();
        JobRole javaRole = roleService.getRoleById("java-developer");

        try (FileInputStream fis = new FileInputStream(sampleJavaResumeFile)) {
            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",
                    "Sample_Java_Developer_Resume.pdf",
                    "application/pdf",
                    fis
            );

            ExtractedPdfData data = extractionService.extractText(mockFile);
            List<String> allDetected = skillService.detectAllSkills(data.getRawText());

            assertTrue(allDetected.contains("Core Java") || allDetected.contains("Java"));
            assertTrue(allDetected.contains("Spring Boot"));
            assertTrue(allDetected.contains("SQL"));
            assertTrue(allDetected.contains("REST APIs"));

            SkillMatchingService.SkillEvaluationResult eval = skillService.matchSkills(
                    data.getRawText(),
                    allDetected,
                    javaRole,
                    null
            );

            assertFalse(eval.getMatchedSkills().isEmpty());
            assertTrue(eval.getMatchedSkills().contains("Core Java"));
            assertTrue(eval.getMatchedSkills().contains("Spring Boot"));
        }
    }

    @Test
    void testJuniorResumeGapsIdentified() throws IOException {
        roleService.initRoles();
        JobRole javaRole = roleService.getRoleById("java-developer");

        try (FileInputStream fis = new FileInputStream(sampleJuniorResumeFile)) {
            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",
                    "Sample_Junior_Resume_With_Gaps.pdf",
                    "application/pdf",
                    fis
            );

            ExtractedPdfData data = extractionService.extractText(mockFile);
            List<AtsCheckItem> checklist = atsService.evaluateAtsCompatibility(data);
            int atsScore = atsService.calculateAtsScore(checklist);

            // Junior resume lacks phone and LinkedIn, so ATS score should be lower
            assertTrue(atsScore < 80, "Expected lower ATS score for resume with missing details, got: " + atsScore);

            List<String> allDetected = skillService.detectAllSkills(data.getRawText());
            SkillMatchingService.SkillEvaluationResult eval = skillService.matchSkills(
                    data.getRawText(),
                    allDetected,
                    javaRole,
                    null
            );

            // Junior resume is missing Spring Boot, SQL, Git, etc.
            assertTrue(eval.getMissingSkills().contains("Spring Boot"));
            assertTrue(eval.getMissingSkills().contains("SQL"));
        }
    }

    @Test
    void testFullPipelineAndReportGeneration() throws IOException {
        roleService.initRoles();
        ResumeAnalysisService analysisService = new ResumeAnalysisService(
                extractionService, atsService, skillService, roleService, recommendationService
        );
        AnalysisReportService reportService = new AnalysisReportService();

        try (FileInputStream fis = new FileInputStream(sampleJavaResumeFile)) {
            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",
                    "Sample_Java_Developer_Resume.pdf",
                    "application/pdf",
                    fis
            );

            ResumeAnalysisResult result = analysisService.analyzeResume(
                    mockFile, "java-developer", null, null
            );

            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals("Java Developer", result.getTargetJobRole());
            assertTrue(result.getScores().getAtsCompatibilityTotal() > 70);
            assertTrue(result.getScores().getRoleSuitabilityTotal() > 60);
            assertEquals(50, result.getScores().getSkillsScore()); // all required skills matched
            assertFalse(result.getRecommendations().getRecommendedProjects().isEmpty());
            assertFalse(result.getRecommendations().getRecommendedCertifications().isEmpty());

            // Test PDF report generation
            byte[] pdfBytes = reportService.generatePdfReport(result);
            assertNotNull(pdfBytes);
            assertTrue(pdfBytes.length > 500, "Generated PDF should have content");

            // Test Text report generation
            String textReport = reportService.generateTextReport(result);
            assertNotNull(textReport);
            assertTrue(textReport.contains("ATS RESUME ANALYSIS & CAREER GUIDANCE REPORT"));
            assertTrue(textReport.contains("Role Suitability Score:"));
        }
    }
}
