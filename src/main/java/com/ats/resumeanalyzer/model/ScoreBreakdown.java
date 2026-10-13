package com.ats.resumeanalyzer.model;

/**
 * Transparent scoring breakdown for both ATS compatibility and Role suitability.
 */
public class ScoreBreakdown {
    // Role Suitability Components (Total 100 marks)
    private int skillsScore;               // Max 50 marks
    private int projectsAndExperienceScore; // Max 30 marks
    private int qualificationsAndCertsScore;// Max 20 marks
    private int roleSuitabilityTotal;      // Max 100

    // ATS Compatibility Score (Total 100 marks)
    private int atsCompatibilityTotal;     // Max 100

    private String roleScoreExplanation;
    private String atsScoreExplanation;

    public ScoreBreakdown() {}

    public ScoreBreakdown(int skillsScore, int projectsAndExperienceScore, int qualificationsAndCertsScore, int atsCompatibilityTotal, String roleScoreExplanation, String atsScoreExplanation) {
        this.skillsScore = skillsScore;
        this.projectsAndExperienceScore = projectsAndExperienceScore;
        this.qualificationsAndCertsScore = qualificationsAndCertsScore;
        this.roleSuitabilityTotal = skillsScore + projectsAndExperienceScore + qualificationsAndCertsScore;
        this.atsCompatibilityTotal = atsCompatibilityTotal;
        this.roleScoreExplanation = roleScoreExplanation;
        this.atsScoreExplanation = atsScoreExplanation;
    }

    public int getSkillsScore() {
        return skillsScore;
    }

    public void setSkillsScore(int skillsScore) {
        this.skillsScore = skillsScore;
        recalculateRoleTotal();
    }

    public int getProjectsAndExperienceScore() {
        return projectsAndExperienceScore;
    }

    public void setProjectsAndExperienceScore(int projectsAndExperienceScore) {
        this.projectsAndExperienceScore = projectsAndExperienceScore;
        recalculateRoleTotal();
    }

    public int getQualificationsAndCertsScore() {
        return qualificationsAndCertsScore;
    }

    public void setQualificationsAndCertsScore(int qualificationsAndCertsScore) {
        this.qualificationsAndCertsScore = qualificationsAndCertsScore;
        recalculateRoleTotal();
    }

    public int getRoleSuitabilityTotal() {
        return roleSuitabilityTotal;
    }

    public void setRoleSuitabilityTotal(int roleSuitabilityTotal) {
        this.roleSuitabilityTotal = roleSuitabilityTotal;
    }

    public int getAtsCompatibilityTotal() {
        return atsCompatibilityTotal;
    }

    public void setAtsCompatibilityTotal(int atsCompatibilityTotal) {
        this.atsCompatibilityTotal = atsCompatibilityTotal;
    }

    public String getRoleScoreExplanation() {
        return roleScoreExplanation;
    }

    public void setRoleScoreExplanation(String roleScoreExplanation) {
        this.roleScoreExplanation = roleScoreExplanation;
    }

    public String getAtsScoreExplanation() {
        return atsScoreExplanation;
    }

    public void setAtsScoreExplanation(String atsScoreExplanation) {
        this.atsScoreExplanation = atsScoreExplanation;
    }

    private void recalculateRoleTotal() {
        this.roleSuitabilityTotal = Math.min(100, Math.max(0, this.skillsScore + this.projectsAndExperienceScore + this.qualificationsAndCertsScore));
    }
}
