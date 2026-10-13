package com.ats.resumeanalyzer.service;

import com.ats.resumeanalyzer.model.JobRole;
import com.ats.resumeanalyzer.model.SkillMatchItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Service providing skill recognition, keyword synonym normalization,
 * role requirement matching, and custom job description comparison.
 */
@Service
public class SkillMatchingService {

    private static final Logger logger = LoggerFactory.getLogger(SkillMatchingService.class);

    // Dictionary of canonical skills and their common variants / aliases
    private final Map<String, List<String>> canonicalSkills = new LinkedHashMap<>();

    public SkillMatchingService() {
        registerSkill("Core Java", "core java", "java se", "java 17", "java 11", "java 8", "java programming", "jdk", "java");
        registerSkill("OOP", "oop", "oops", "object oriented programming", "object-oriented programming", "object oriented", "polymorphism", "inheritance");
        registerSkill("Collections Framework", "collections framework", "java collections", "arraylist", "hashmap", "collections");
        registerSkill("Spring Boot", "spring boot", "springboot", "spring-boot");
        registerSkill("Spring Framework", "spring framework", "spring mvc", "spring data", "spring security");
        registerSkill("REST APIs", "rest api", "rest apis", "restful", "restful api", "restful apis", "restful web services", "rest web services", "api development", "rest");
        registerSkill("SQL", "sql", "mysql", "postgresql", "postgres", "sqlite", "oracle db", "ms sql", "relational database", "pl/sql");
        registerSkill("Hibernate / JPA", "hibernate", "jpa", "spring data jpa", "orm");
        registerSkill("Git", "git", "github", "gitlab", "version control", "bitbucket");
        registerSkill("Docker", "docker", "containerization", "docker-compose");
        registerSkill("Microservices", "microservices", "microservice architecture", "distributed systems");
        registerSkill("Multithreading", "multithreading", "concurrency", "thread pool", "parallel processing");
        registerSkill("Exception Handling", "exception handling", "custom exceptions", "try-catch");
        registerSkill("Maven", "maven", "pom.xml", "gradle");
        registerSkill("JUnit / Mockito", "junit", "junit 5", "mockito", "unit testing", "test-driven development", "tdd");

        // Frontend
        registerSkill("HTML5", "html5", "html");
        registerSkill("CSS3", "css3", "css", "sass", "scss");
        registerSkill("JavaScript", "javascript", "js", "es6", "es6+", "vanilla js");
        registerSkill("TypeScript", "typescript", "ts");
        registerSkill("React", "react", "react.js", "reactjs");
        registerSkill("Redux", "redux", "redux toolkit", "state management", "zustand");
        registerSkill("Tailwind CSS", "tailwind css", "tailwind", "bootstrap");
        registerSkill("Responsive Design", "responsive design", "mobile-first", "flexbox", "css grid");
        registerSkill("Next.js", "next.js", "nextjs");

        // Python
        registerSkill("Python", "python", "python 3", "python3");
        registerSkill("Django / FastAPI", "django", "fastapi", "flask", "django rest framework", "drf");
        registerSkill("Pandas", "pandas");
        registerSkill("NumPy", "numpy");
        registerSkill("pytest", "pytest", "unittest");

        // Data Analytics
        registerSkill("Data Visualization", "data visualization", "matplotlib", "seaborn", "plotly");
        registerSkill("Power BI / Tableau", "power bi", "powerbi", "tableau", "dax");
        registerSkill("Excel / Spreadsheets", "excel", "advanced excel", "spreadsheets", "pivot tables", "vlookup");
        registerSkill("Data Cleaning", "data cleaning", "data preprocessing", "eda", "exploratory data analysis");
        registerSkill("Statistics", "statistics", "statistical analysis", "hypothesis testing");

        // General Engineering
        registerSkill("Data Structures & Algorithms", "data structures", "algorithms", "dsa", "leetcode", "big o");
        registerSkill("Problem Solving", "problem solving", "analytical thinking", "debugging");
        registerSkill("Linux", "linux", "bash", "shell scripting", "unix");
        registerSkill("CI/CD", "ci/cd", "continuous integration", "github actions", "jenkins");
        registerSkill("AWS", "aws", "amazon web services", "cloud computing");
    }

    private void registerSkill(String canonical, String... aliases) {
        List<String> list = new ArrayList<>();
        list.add(canonical.toLowerCase());
        for (String a : aliases) {
            list.add(a.toLowerCase());
        }
        canonicalSkills.put(canonical, list);
    }

    /**
     * Extracts all known skills present in the resume text.
     */
    public List<String> detectAllSkills(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String lowerText = text.toLowerCase();
        List<String> detected = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : canonicalSkills.entrySet()) {
            String canonical = entry.getKey();
            List<String> aliases = entry.getValue();

            boolean found = false;
            for (String alias : aliases) {
                if (containsKeyword(lowerText, alias)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                detected.add(canonical);
            }
        }

        return detected;
    }

    /**
     * Compares the target role's skills (and optional job description) with detected skills.
     */
    public SkillEvaluationResult matchSkills(String resumeText, List<String> detectedSkills, JobRole targetRole, String customJobDescription) {
        Set<String> detectedSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        detectedSet.addAll(detectedSkills);

        List<String> requiredList = new ArrayList<>(targetRole.getRequiredSkills());
        List<String> secondaryList = new ArrayList<>(targetRole.getSecondarySkills());

        // If a custom job description is provided, extract extra requirements from it
        if (customJobDescription != null && !customJobDescription.isBlank()) {
            List<String> jdSkills = extractSkillsFromJobDescription(customJobDescription);
            for (String s : jdSkills) {
                if (!requiredList.contains(s) && !secondaryList.contains(s)) {
                    requiredList.add(s);
                }
            }
        }

        List<SkillMatchItem> skillMatchItems = new ArrayList<>();
        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();

        // Process required skills
        for (String skill : requiredList) {
            boolean matched = isSkillPresent(resumeText, detectedSet, skill);
            String snippet = matched ? findEvidenceSnippet(resumeText, skill) : null;

            skillMatchItems.add(new SkillMatchItem(skill, matched, true, "Core Requirement", snippet));
            if (matched) {
                matchedSkills.add(skill);
            } else {
                missingSkills.add(skill);
            }
        }

        // Process secondary / nice-to-have skills
        for (String skill : secondaryList) {
            boolean matched = isSkillPresent(resumeText, detectedSet, skill);
            String snippet = matched ? findEvidenceSnippet(resumeText, skill) : null;

            skillMatchItems.add(new SkillMatchItem(skill, matched, false, "Recommended Skill", snippet));
            if (matched && !matchedSkills.contains(skill)) {
                matchedSkills.add(skill);
            } else if (!matched && !missingSkills.contains(skill)) {
                missingSkills.add(skill);
            }
        }

        return new SkillEvaluationResult(skillMatchItems, matchedSkills, missingSkills);
    }

    /**
     * Extracts recognized skills from custom job description text.
     */
    public List<String> extractSkillsFromJobDescription(String jdText) {
        if (jdText == null || jdText.isBlank()) {
            return Collections.emptyList();
        }

        String lowerJd = jdText.toLowerCase();
        List<String> extracted = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : canonicalSkills.entrySet()) {
            for (String alias : entry.getValue()) {
                if (containsKeyword(lowerJd, alias)) {
                    extracted.add(entry.getKey());
                    break;
                }
            }
        }

        return extracted;
    }

    /**
     * Checks if a skill is present either in the detected set or directly in raw resume text.
     */
    private boolean isSkillPresent(String resumeText, Set<String> detectedSet, String skill) {
        if (detectedSet.contains(skill)) {
            return true;
        }

        List<String> aliases = canonicalSkills.get(skill);
        if (aliases != null) {
            for (String alias : aliases) {
                if (containsKeyword(resumeText.toLowerCase(), alias)) {
                    return true;
                }
            }
        }

        return containsKeyword(resumeText.toLowerCase(), skill.toLowerCase());
    }

    /**
     * Finds an evidence snippet surrounding the matched skill keyword.
     */
    private String findEvidenceSnippet(String text, String skill) {
        if (text == null) return null;
        String lower = text.toLowerCase();
        int idx = lower.indexOf(skill.toLowerCase());

        if (idx == -1) {
            List<String> aliases = canonicalSkills.get(skill);
            if (aliases != null) {
                for (String a : aliases) {
                    idx = lower.indexOf(a.toLowerCase());
                    if (idx != -1) break;
                }
            }
        }

        if (idx != -1) {
            int start = Math.max(0, idx - 40);
            int end = Math.min(text.length(), idx + 60);
            String snippet = text.substring(start, end).replaceAll("[\r\n]+", " ").trim();
            return "..." + snippet + "...";
        }

        return "Detected in resume content";
    }

    /**
     * Boundary-aware keyword matching helper.
     */
    private boolean containsKeyword(String text, String keyword) {
        if (text == null || keyword == null || keyword.isEmpty()) return false;
        
        // Exact substring with non-alphanumeric boundary check
        String regex = "(?i)(^|[^a-zA-Z0-9+#])" + Pattern.quote(keyword) + "([^a-zA-Z0-9+#]|$)";
        return Pattern.compile(regex).matcher(text).find();
    }

    /**
     * Helper encapsulation of match outcome.
     */
    public static class SkillEvaluationResult {
        private final List<SkillMatchItem> matchItems;
        private final List<String> matchedSkills;
        private final List<String> missingSkills;

        public SkillEvaluationResult(List<SkillMatchItem> matchItems, List<String> matchedSkills, List<String> missingSkills) {
            this.matchItems = matchItems;
            this.matchedSkills = matchedSkills;
            this.missingSkills = missingSkills;
        }

        public List<SkillMatchItem> getMatchItems() {
            return matchItems;
        }

        public List<String> getMatchedSkills() {
            return matchedSkills;
        }

        public List<String> getMissingSkills() {
            return missingSkills;
        }
    }
}
