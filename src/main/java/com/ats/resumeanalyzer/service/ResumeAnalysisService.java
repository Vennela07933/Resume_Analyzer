package com.ats.resumeanalyzer.service;

import com.ats.resumeanalyzer.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Core orchestrator service that executes the full resume analysis pipeline:
 * 1. PDF text extraction & section segmentation
 * 2. ATS compatibility verification
 * 3. Skill matching against target job role / custom job description
 * 4. Dual scoring computation (ATS Compatibility & Role Suitability)
 * 5. Career recommendations & improvement suggestions
 */
@Service
public class ResumeAnalysisService {

    private static final Logger logger = LoggerFactory.getLogger(ResumeAnalysisService.class);

    private final PdfTextExtractionService pdfTextExtractionService;
    private final AtsCompatibilityService atsCompatibilityService;
    private final SkillMatchingService skillMatchingService;
    private final JobRoleService jobRoleService;
    private final CareerRecommendationService careerRecommendationService;

    // In-memory cache for recent analysis sessions to support on-demand report download
    private final Map<String, ResumeAnalysisResult> sessionStore = new ConcurrentHashMap<>();

    public ResumeAnalysisService(
            PdfTextExtractionService pdfTextExtractionService,
            AtsCompatibilityService atsCompatibilityService,
            SkillMatchingService skillMatchingService,
            JobRoleService jobRoleService,
            CareerRecommendationService careerRecommendationService) {
        this.pdfTextExtractionService = pdfTextExtractionService;
        this.atsCompatibilityService = atsCompatibilityService;
        this.skillMatchingService = skillMatchingService;
        this.jobRoleService = jobRoleService;
        this.careerRecommendationService = careerRecommendationService;
    }

    /**
     * Executes the complete analysis pipeline for an uploaded PDF resume.
     *
     * @param file the uploaded resume file
     * @param targetRoleId the target role id (e.g. "java-developer", or "custom")
     * @param customRoleTitle optional custom role title if user specified custom
     * @param customJobDescription optional user-pasted job description text
     * @return ResumeAnalysisResult complete analysis report
     */
    public ResumeAnalysisResult analyzeResume(
            MultipartFile file,
            String targetRoleId,
            String customRoleTitle,
            String customJobDescription) {

        logger.info("Starting resume analysis: file='{}', roleId='{}', customTitle='{}', hasJd={}",
                file != null ? file.getOriginalFilename() : "null", targetRoleId, customRoleTitle,
                (customJobDescription != null && !customJobDescription.isBlank()));

        // Step 1: Text extraction & structure parsing
        ExtractedPdfData extractedData = pdfTextExtractionService.extractText(file);

        // Step 2: Determine Target Job Role
        JobRole targetRole;
        boolean isCustomRole = false;
        if (customRoleTitle != null && !customRoleTitle.isBlank()) {
            targetRole = jobRoleService.createCustomRole(customRoleTitle.trim());
            isCustomRole = true;
        } else if ("custom".equalsIgnoreCase(targetRoleId)) {
            targetRole = jobRoleService.createCustomRole("Custom Software Role");
            isCustomRole = true;
        } else {
            targetRole = jobRoleService.getRoleById(targetRoleId);
        }

        // Step 3: ATS Compatibility Checklist & Score Calculation
        List<AtsCheckItem> atsChecklist = atsCompatibilityService.evaluateAtsCompatibility(extractedData);
        int atsCompatibilityScore = atsCompatibilityService.calculateAtsScore(atsChecklist);

        // Step 4: Skill Detection & Role Matching
        List<String> allDetectedSkills = skillMatchingService.detectAllSkills(extractedData.getRawText());
        SkillMatchingService.SkillEvaluationResult skillEval = skillMatchingService.matchSkills(
                extractedData.getRawText(),
                allDetectedSkills,
                targetRole,
                customJobDescription
        );

        // Step 5: Dual Score Calculation (Role Suitability Breakdown)
        ScoreBreakdown scores = calculateRoleSuitability(
                targetRole,
                skillEval,
                extractedData,
                atsCompatibilityScore
        );

        // Step 6: Career Guidance and Recommendations
        CareerRecommendation recommendations = careerRecommendationService.generateRecommendations(
                targetRole,
                skillEval.getMissingSkills(),
                skillEval.getMatchedSkills(),
                extractedData,
                atsChecklist
        );

        // Step 7: Build detected sections list for frontend visualization
        List<ResumeSection> sectionsList = buildSectionList(extractedData.getSectionTexts());

        // Step 8: Assemble final result object
        String analysisId = UUID.randomUUID().toString();
        ResumeAnalysisResult result = new ResumeAnalysisResult();
        result.setId(analysisId);
        result.setFileName(extractedData.getFileName());
        result.setFileSizeBytes(extractedData.getFileSizeBytes());
        result.setPageCount(extractedData.getPageCount());
        result.setTotalWordCount(extractedData.getWordCount());
        result.setTargetJobRole(targetRole.getTitle());
        result.setCustomRoleUsed(isCustomRole);
        result.setJobDescriptionProvided(customJobDescription != null && !customJobDescription.isBlank());

        result.setContactInfo(extractedData.getContactInfo());
        result.setSections(sectionsList);
        result.setAllDetectedSkills(allDetectedSkills);
        result.setSkillMatches(skillEval.getMatchItems());
        result.setMatchedSkills(skillEval.getMatchedSkills());
        result.setMissingSkills(skillEval.getMissingSkills());

        result.setAtsChecklist(atsChecklist);
        result.setScores(scores);
        result.setRecommendations(recommendations);
        result.setScannedPdfWarning(extractedData.isLikelyScanned());
        result.setAnalysisTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Cache for download service (retains up to 100 sessions)
        if (sessionStore.size() > 100) {
            sessionStore.clear();
        }
        sessionStore.put(analysisId, result);

        logger.info("Resume analysis finished: id={}, ATS Score={}, Role Suitability={}",
                analysisId, atsCompatibilityScore, scores.getRoleSuitabilityTotal());

        return result;
    }

    /**
     * Calculates the transparent Role Suitability Score out of 100:
     * - Required skills match: 50 marks
     * - Relevant projects and experience: 30 marks
     * - Evidence of role-related qualifications or certifications: 20 marks
     */
    private ScoreBreakdown calculateRoleSuitability(
            JobRole targetRole,
            SkillMatchingService.SkillEvaluationResult skillEval,
            ExtractedPdfData extractedData,
            int atsScore) {

        // 1. Required Skills Match (Max 50 marks)
        int totalRequired = 0;
        int matchedRequired = 0;
        for (SkillMatchItem item : skillEval.getMatchItems()) {
            if (item.isRequired()) {
                totalRequired++;
                if (item.isMatched()) {
                    matchedRequired++;
                }
            }
        }

        int skillsScore;
        if (totalRequired > 0) {
            double skillRatio = (double) matchedRequired / totalRequired;
            skillsScore = (int) Math.round(skillRatio * 50.0);
        } else {
            skillsScore = 30; // Fallback if no specific requirements
        }

        // 2. Relevant Projects and Experience (Max 30 marks)
        Map<String, String> sections = extractedData.getSectionTexts();
        int projectsAndExpScore = 0;

        boolean hasProjects = sections.containsKey("Projects") && sections.get("Projects").length() > 50;
        boolean hasExperience = sections.containsKey("Experience") && sections.get("Experience").length() > 50;

        if (hasProjects) {
            projectsAndExpScore += 15;
        }
        if (hasExperience) {
            projectsAndExpScore += 15;
        } else if (hasProjects && sections.get("Projects").length() > 250) {
            // For students without full-time jobs, substantial technical projects provide compensatory weight
            projectsAndExpScore += 10;
        }

        // Ensure within 0 to 30
        projectsAndExpScore = Math.min(30, Math.max(0, projectsAndExpScore));

        // 3. Qualifications and Certifications (Max 20 marks)
        int qualAndCertsScore = 0;
        String rawLower = extractedData.getRawText().toLowerCase();

        // Degree in relevant STEM / CS discipline (10 marks)
        boolean hasDegree = rawLower.matches(".*\\b(b\\.tech|b\\.e\\.|bachelor|master|m\\.tech|bca|mca|b\\.sc|computer science|information technology|engineering)\\b.*");
        if (hasDegree) {
            qualAndCertsScore += 10;
        }

        // Certifications / Courses / Academic Achievements (10 marks)
        boolean hasCerts = sections.containsKey("Certifications") || sections.containsKey("Achievements")
                || rawLower.matches(".*\\b(certified|certification|certificate|coursera|udemy|hackerank|leetcode|oracle certified)\\b.*");
        if (hasCerts) {
            qualAndCertsScore += 10;
        }

        qualAndCertsScore = Math.min(20, Math.max(0, qualAndCertsScore));

        // Construct human-readable explanations
        String roleExplanation = String.format(
                "Role Suitability: %d/100 points calculated as: Skills Match (%d/50 pts, %d of %d required skills found) + Projects & Experience (%d/30 pts) + Qualifications & Certifications (%d/20 pts).",
                (skillsScore + projectsAndExpScore + qualAndCertsScore),
                skillsScore, matchedRequired, totalRequired,
                projectsAndExpScore,
                qualAndCertsScore
        );

        String atsExplanation = String.format(
                "ATS Compatibility: %d/100 points computed across 9 rule-based automated parsing checks including file readability, standard section headers, contact completeness, and text structure.",
                atsScore
        );

        return new ScoreBreakdown(
                skillsScore,
                projectsAndExpScore,
                qualAndCertsScore,
                atsScore,
                roleExplanation,
                atsExplanation
        );
    }

    /**
     * Builds structured section previews for UI rendering.
     */
    private List<ResumeSection> buildSectionList(Map<String, String> sectionTexts) {
        List<ResumeSection> list = new ArrayList<>();
        String[] standardSections = {"Education", "Skills", "Experience", "Projects", "Certifications", "Achievements", "Summary"};

        for (String secName : standardSections) {
            boolean detected = sectionTexts.containsKey(secName);
            String text = sectionTexts.getOrDefault(secName, "");
            String preview = "";
            int wordCount = 0;

            if (detected && !text.isBlank()) {
                String[] words = text.trim().split("\\s+");
                wordCount = words.length;
                preview = text.length() > 180 ? text.substring(0, 180).replaceAll("[\r\n]+", " ") + "..." : text.replaceAll("[\r\n]+", " ");
            }

            list.add(new ResumeSection(secName, detected, secName, preview, wordCount));
        }

        return list;
    }

    /**
     * Retrieves a stored analysis result by its UUID.
     */
    public ResumeAnalysisResult getAnalysisResult(String analysisId) {
        return sessionStore.get(analysisId);
    }
}
