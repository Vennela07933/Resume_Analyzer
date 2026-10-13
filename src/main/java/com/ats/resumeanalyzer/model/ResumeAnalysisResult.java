package com.ats.resumeanalyzer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Complete outcome of a resume analysis session.
 */
public class ResumeAnalysisResult {
    private String id;
    private String fileName;
    private long fileSizeBytes;
    private int pageCount;
    private int totalWordCount;
    private String targetJobRole;
    private boolean customRoleUsed;
    private boolean jobDescriptionProvided;

    private ContactInfo contactInfo = new ContactInfo();
    private List<ResumeSection> sections = new ArrayList<>();
    private List<String> allDetectedSkills = new ArrayList<>();
    private List<SkillMatchItem> skillMatches = new ArrayList<>();
    private List<String> matchedSkills = new ArrayList<>();
    private List<String> missingSkills = new ArrayList<>();

    private List<AtsCheckItem> atsChecklist = new ArrayList<>();
    private ScoreBreakdown scores = new ScoreBreakdown();
    private CareerRecommendation recommendations = new CareerRecommendation();

    private boolean scannedPdfWarning;
    private String analysisTimestamp;
    private String disclaimer = "Notice: Scores and suggestions generated are rule-based approximations designed to help improve resume structure and keyword alignment. They are not official hiring decisions or employment guarantees.";

    public ResumeAnalysisResult() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalWordCount() {
        return totalWordCount;
    }

    public void setTotalWordCount(int totalWordCount) {
        this.totalWordCount = totalWordCount;
    }

    public String getTargetJobRole() {
        return targetJobRole;
    }

    public void setTargetJobRole(String targetJobRole) {
        this.targetJobRole = targetJobRole;
    }

    public boolean isCustomRoleUsed() {
        return customRoleUsed;
    }

    public void setCustomRoleUsed(boolean customRoleUsed) {
        this.customRoleUsed = customRoleUsed;
    }

    public boolean isJobDescriptionProvided() {
        return jobDescriptionProvided;
    }

    public void setJobDescriptionProvided(boolean jobDescriptionProvided) {
        this.jobDescriptionProvided = jobDescriptionProvided;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<ResumeSection> getSections() {
        return sections;
    }

    public void setSections(List<ResumeSection> sections) {
        this.sections = sections;
    }

    public List<String> getAllDetectedSkills() {
        return allDetectedSkills;
    }

    public void setAllDetectedSkills(List<String> allDetectedSkills) {
        this.allDetectedSkills = allDetectedSkills;
    }

    public List<SkillMatchItem> getSkillMatches() {
        return skillMatches;
    }

    public void setSkillMatches(List<SkillMatchItem> skillMatches) {
        this.skillMatches = skillMatches;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public List<AtsCheckItem> getAtsChecklist() {
        return atsChecklist;
    }

    public void setAtsChecklist(List<AtsCheckItem> atsChecklist) {
        this.atsChecklist = atsChecklist;
    }

    public ScoreBreakdown getScores() {
        return scores;
    }

    public void setScores(ScoreBreakdown scores) {
        this.scores = scores;
    }

    public CareerRecommendation getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(CareerRecommendation recommendations) {
        this.recommendations = recommendations;
    }

    public boolean isScannedPdfWarning() {
        return scannedPdfWarning;
    }

    public void setScannedPdfWarning(boolean scannedPdfWarning) {
        this.scannedPdfWarning = scannedPdfWarning;
    }

    public String getAnalysisTimestamp() {
        return analysisTimestamp;
    }

    public void setAnalysisTimestamp(String analysisTimestamp) {
        this.analysisTimestamp = analysisTimestamp;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
