package com.ats.resumeanalyzer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Beginner-friendly and portfolio-ready project recommendation tailored to address identified skill gaps.
 */
public class ProjectRecommendation {
    private String title;
    private String description;
    private String complexity; // "Beginner-Friendly", "Intermediate"
    private List<String> keyTechnologies = new ArrayList<>();
    private List<String> learningOutcomes = new ArrayList<>();
    private String suggestedResumeBullet;

    public ProjectRecommendation() {}

    public ProjectRecommendation(String title, String description, String complexity, List<String> keyTechnologies, List<String> learningOutcomes, String suggestedResumeBullet) {
        this.title = title;
        this.description = description;
        this.complexity = complexity;
        this.keyTechnologies = keyTechnologies;
        this.learningOutcomes = learningOutcomes;
        this.suggestedResumeBullet = suggestedResumeBullet;
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

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public List<String> getKeyTechnologies() {
        return keyTechnologies;
    }

    public void setKeyTechnologies(List<String> keyTechnologies) {
        this.keyTechnologies = keyTechnologies;
    }

    public List<String> getLearningOutcomes() {
        return learningOutcomes;
    }

    public void setLearningOutcomes(List<String> learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }

    public String getSuggestedResumeBullet() {
        return suggestedResumeBullet;
    }

    public void setSuggestedResumeBullet(String suggestedResumeBullet) {
        this.suggestedResumeBullet = suggestedResumeBullet;
    }
}
