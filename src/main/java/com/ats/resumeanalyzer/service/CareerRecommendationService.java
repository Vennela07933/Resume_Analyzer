package com.ats.resumeanalyzer.service;

import com.ats.resumeanalyzer.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Generates personalized career guidance, authentic certification suggestions,
 * portfolio project ideas, and actionable resume writing improvements.
 */
@Service
public class CareerRecommendationService {

    /**
     * Builds career guidance recommendations based on the target role, identified skill gaps,
     * and ATS evaluation results.
     */
    public CareerRecommendation generateRecommendations(
            JobRole targetRole,
            List<String> missingSkills,
            List<String> matchedSkills,
            ExtractedPdfData extractedData,
            List<AtsCheckItem> atsChecklist) {

        // 1. Skills to Learn
        List<String> skillsToLearn = new ArrayList<>(missingSkills);

        // 2. Certifications
        List<CertificationItem> certifications = new ArrayList<>(targetRole.getRecommendedCertifications());

        // 3. Project Recommendations
        List<ProjectRecommendation> projects = new ArrayList<>(targetRole.getRecommendedProjects());

        // 4. Resume Improvement Suggestions
        List<String> improvementSuggestions = generateResumeSuggestions(extractedData, atsChecklist, missingSkills);

        return new CareerRecommendation(skillsToLearn, certifications, projects, improvementSuggestions);
    }

    /**
     * Generates concrete, ethical resume writing improvements tailored to the candidate's content.
     */
    private List<String> generateResumeSuggestions(
            ExtractedPdfData extractedData,
            List<AtsCheckItem> atsChecklist,
            List<String> missingSkills) {

        List<String> suggestions = new ArrayList<>();
        Map<String, String> sections = extractedData.getSectionTexts();
        String rawText = extractedData.getRawText();

        // Check 1: Action Verbs in Experience / Projects
        String projectOrExpText = (sections.getOrDefault("Projects", "") + " " + sections.getOrDefault("Experience", "")).toLowerCase();
        boolean hasActionVerbs = projectOrExpText.matches(".*\\b(developed|engineered|implemented|architected|designed|optimized|built|integrated|automated|spearheaded)\\b.*");

        if (!hasActionVerbs) {
            suggestions.add("Use strong technical action verbs: Begin bullet points with verbs such as 'Engineered', 'Implemented', 'Architected', or 'Optimized' rather than passive phrases like 'Responsible for' or 'Worked on'.");
        } else {
            suggestions.add("Maintain consistency with action verbs: Ensure every project bullet starts with an active past-tense verb showing personal contribution.");
        }

        // Check 2: Quantifiable metrics
        boolean hasMetrics = projectOrExpText.matches(".*\\b(\\d+[%]|\\d+\\+? users|\\d+ms|reduced|increased|improved by)\\b.*");
        if (!hasMetrics) {
            suggestions.add("Add measurable impact and metrics: Quantify truthful achievements where possible (e.g. 'Improved database query response time by 30%', 'Supported 500+ mock users', 'Achieved 85%+ unit test coverage'). Never invent figures, but highlight real milestones.");
        }

        // Check 3: Technology stack listing per project
        if (sections.containsKey("Projects")) {
            suggestions.add("Clearly list technologies under each project: Format project headers with tools used, e.g. 'E-Commerce Platform | Java, Spring Boot, MySQL, Docker'. This ensures ATS algorithms associate skills with hands-on practice.");
        } else {
            suggestions.add("Add a designated 'Projects' section: Detail 2 to 3 practical applications highlighting your role, architecture decisions, and technologies used.");
        }

        // Check 4: Addressing skill gaps
        if (!missingSkills.isEmpty()) {
            String topMissing = String.join(", ", missingSkills.subList(0, Math.min(3, missingSkills.size())));
            suggestions.add("Target primary skill gaps (" + topMissing + "): Build a focused mini-project or complete a tutorial on these topics so you can legitimately include them in your skills and project sections.");
        }

        // Check 5: ATS Formatting & Layout
        for (AtsCheckItem item : atsChecklist) {
            if (item.getStatus() == AtsCheckStatus.FAILED || item.getStatus() == AtsCheckStatus.WARNING) {
                if (item.getId().equals("ATS-04")) {
                    suggestions.add("Provide direct repository links: Include your GitHub handle or live project URLs directly under the project title for easy reviewer verification.");
                } else if (item.getId().equals("ATS-06")) {
                    suggestions.add("Group your Technical Skills: Categorize skills into logical groups (e.g. Languages, Frameworks, Developer Tools, Databases) so both scanners and human recruiters can quickly scan them.");
                }
            }
        }

        suggestions.add("Format descriptions using the XYZ formula: 'Accomplished [X] as measured by [Y] by doing [Z]'. For example: 'Built a RESTful API [X] reducing lookup latency to under 40ms [Y] by indexing SQL tables and caching frequently accessed queries [Z]'.");

        return suggestions;
    }
}
