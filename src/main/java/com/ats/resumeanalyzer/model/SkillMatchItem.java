package com.ats.resumeanalyzer.model;

/**
 * Represents comparison of an individual skill between job requirements and resume text.
 */
public class SkillMatchItem {
    private String skillName;
    private boolean matched;
    private boolean required;
    private String category;
    private String evidenceSnippet;

    public SkillMatchItem() {}

    public SkillMatchItem(String skillName, boolean matched, boolean required, String category, String evidenceSnippet) {
        this.skillName = skillName;
        this.matched = matched;
        this.required = required;
        this.category = category;
        this.evidenceSnippet = evidenceSnippet;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEvidenceSnippet() {
        return evidenceSnippet;
    }

    public void setEvidenceSnippet(String evidenceSnippet) {
        this.evidenceSnippet = evidenceSnippet;
    }
}
