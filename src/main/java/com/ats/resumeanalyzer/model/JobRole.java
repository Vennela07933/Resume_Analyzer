package com.ats.resumeanalyzer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a job role profile against which resumes are evaluated.
 */
public class JobRole {
    private String id;
    private String title;
    private String description;
    private List<String> requiredSkills = new ArrayList<>();
    private List<String> secondarySkills = new ArrayList<>();
    private List<CertificationItem> recommendedCertifications = new ArrayList<>();
    private List<ProjectRecommendation> recommendedProjects = new ArrayList<>();
    private List<String> generalTips = new ArrayList<>();

    public JobRole() {}

    public JobRole(String id, String title, String description, List<String> requiredSkills, List<String> secondarySkills) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
        this.secondarySkills = secondarySkills != null ? secondarySkills : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public List<String> getSecondarySkills() {
        return secondarySkills;
    }

    public void setSecondarySkills(List<String> secondarySkills) {
        this.secondarySkills = secondarySkills;
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

    public List<String> getGeneralTips() {
        return generalTips;
    }

    public void setGeneralTips(List<String> generalTips) {
        this.generalTips = generalTips;
    }
}
