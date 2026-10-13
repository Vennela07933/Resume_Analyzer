package com.ats.resumeanalyzer.model;

/**
 * Details of an individual ATS compatibility test rule.
 */
public class AtsCheckItem {
    private String id;
    private String ruleName;
    private String category;
    private AtsCheckStatus status;
    private int weight;
    private int scoreEarned;
    private String details;
    private String recommendation;

    public AtsCheckItem() {}

    public AtsCheckItem(String id, String ruleName, String category, AtsCheckStatus status, int weight, int scoreEarned, String details, String recommendation) {
        this.id = id;
        this.ruleName = ruleName;
        this.category = category;
        this.status = status;
        this.weight = weight;
        this.scoreEarned = scoreEarned;
        this.details = details;
        this.recommendation = recommendation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public AtsCheckStatus getStatus() {
        return status;
    }

    public void setStatus(AtsCheckStatus status) {
        this.status = status;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getScoreEarned() {
        return scoreEarned;
    }

    public void setScoreEarned(int scoreEarned) {
        this.scoreEarned = scoreEarned;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
