package com.ats.resumeanalyzer.model;

/**
 * Encapsulates detected contact information in the candidate resume.
 */
public class ContactInfo {
    private String candidateName;
    private String email;
    private String phone;
    private String linkedInUrl;
    private String gitHubUrl;
    private String portfolioUrl;
    private boolean emailFound;
    private boolean phoneFound;
    private boolean linksFound;

    public ContactInfo() {}

    public ContactInfo(String candidateName, String email, String phone, String linkedInUrl, String gitHubUrl, String portfolioUrl) {
        this.candidateName = candidateName;
        this.email = email;
        this.phone = phone;
        this.linkedInUrl = linkedInUrl;
        this.gitHubUrl = gitHubUrl;
        this.portfolioUrl = portfolioUrl;
        this.emailFound = email != null && !email.isBlank();
        this.phoneFound = phone != null && !phone.isBlank();
        this.linksFound = (linkedInUrl != null && !linkedInUrl.isBlank()) || (gitHubUrl != null && !gitHubUrl.isBlank()) || (portfolioUrl != null && !portfolioUrl.isBlank());
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.emailFound = email != null && !email.isBlank();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.phoneFound = phone != null && !phone.isBlank();
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
        updateLinksFound();
    }

    public String getGitHubUrl() {
        return gitHubUrl;
    }

    public void setGitHubUrl(String gitHubUrl) {
        this.gitHubUrl = gitHubUrl;
        updateLinksFound();
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
        updateLinksFound();
    }

    public boolean isEmailFound() {
        return emailFound;
    }

    public void setEmailFound(boolean emailFound) {
        this.emailFound = emailFound;
    }

    public boolean isPhoneFound() {
        return phoneFound;
    }

    public void setPhoneFound(boolean phoneFound) {
        this.phoneFound = phoneFound;
    }

    public boolean isLinksFound() {
        return (linkedInUrl != null && !linkedInUrl.isBlank())
                || (gitHubUrl != null && !gitHubUrl.isBlank())
                || (portfolioUrl != null && !portfolioUrl.isBlank());
    }

    public void setLinksFound(boolean linksFound) {
        this.linksFound = linksFound;
    }

    private void updateLinksFound() {
        this.linksFound = isLinksFound();
    }
}
