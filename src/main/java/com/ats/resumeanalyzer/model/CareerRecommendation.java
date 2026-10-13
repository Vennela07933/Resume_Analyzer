package com.ats.resumeanalyzer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Recommendations package generated for the candidate based on detected gaps and role expectations.
 */
public class CareerRecommendation {
    private List<String> skillsToLearn = new ArrayList<>();
    private List<CertificationItem> recommendedCertifications = new ArrayList<>();
    private List<ProjectRecommendation> recommendedProjects = new ArrayList<>();
    private List<String> resumeImprovementSuggestions = new ArrayList<>();

    public CareerRecommendation() {}

    public CareerRecommendation(List<String> skillsToLearn, List<CertificationItem> recommendedCertifications, List<ProjectRecommendation> recommendedProjects, List<String> resumeImprovementSuggestions) {
        this.skillsToLearn = skillsToLearn != null ? skillsToLearn : new ArrayList<>();
        this.recommendedCertifications = recommendedCertifications != null ? recommendedCertifications : new ArrayList<>();
        this.recommendedProjects = recommendedProjects != null ? recommendedProjects : new ArrayList<>();
        this.resumeImprovementSuggestions = resumeImprovementSuggestions != null ? resumeImprovementSuggestions : new ArrayList<>();
    }

    public List<String> getSkillsToLearn() {
        return skillsToLearn;
    }

    public void setSkillsToLearn(List<String> skillsToLearn) {
        this.skillsToLearn = skillsToLearn;
    }

    public List<CertificationItem> getRecommendedCertifications() {
        return recommendedCertifications;
    }

    public void setRecommendedCertifications(List<CertificationItem> recommendedCertifications) {
        this.recommendedCertifications = recommendedCertifications;
    }

    public List<ProjectRecommendation> getRecommendedProjects() {
        return recommendedProjects;
    }

    public void setRecommendedProjects(List<ProjectRecommendation> recommendedProjects) {
        this.recommendedProjects = recommendedProjects;
    }

    public List<String> getResumeImprovementSuggestions() {
        return resumeImprovementSuggestions;
    }

    public void setResumeImprovementSuggestions(List<String> resumeImprovementSuggestions) {
        this.resumeImprovementSuggestions = resumeImprovementSuggestions;
    }
}
