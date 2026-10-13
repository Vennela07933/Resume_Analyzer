package com.ats.resumeanalyzer.model;

/**
 * Legitimate, verified career certification or course recommendation.
 */
public class CertificationItem {
    private String title;
    private String provider;
    private String credentialType; // e.g. "Professional Industry Certification", "Free Course & Certificate", "University Specialization"
    private String costCategory;   // e.g. "Free", "Paid Exam", "Free Audit / Paid Certificate"
    private String officialUrl;
    private String relevance;

    public CertificationItem() {}

    public CertificationItem(String title, String provider, String credentialType, String costCategory, String officialUrl, String relevance) {
        this.title = title;
        this.provider = provider;
        this.credentialType = credentialType;
        this.costCategory = costCategory;
        this.officialUrl = officialUrl;
        this.relevance = relevance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getCostCategory() {
        return costCategory;
    }

    public void setCostCategory(String costCategory) {
        this.costCategory = costCategory;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }
}
