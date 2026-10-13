package com.ats.resumeanalyzer.model;

/**
 * Represents a logical section detected in the resume text.
 */
public class ResumeSection {
    private String name;
    private boolean detected;
    private String standardHeading;
    private String previewText;
    private int wordCount;

    public ResumeSection() {}

    public ResumeSection(String name, boolean detected, String standardHeading, String previewText, int wordCount) {
        this.name = name;
        this.detected = detected;
        this.standardHeading = standardHeading;
        this.previewText = previewText;
        this.wordCount = wordCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDetected() {
        return detected;
    }

    public void setDetected(boolean detected) {
        this.detected = detected;
    }

    public String getStandardHeading() {
        return standardHeading;
    }

    public void setStandardHeading(String standardHeading) {
        this.standardHeading = standardHeading;
    }

    public String getPreviewText() {
        return previewText;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}
