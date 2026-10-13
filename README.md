# ATS-Friendly Resume Analyzer and Career Guidance System

A complete, beginner-friendly web application built with **Java 17**, **Spring Boot 3**, and **Apache PDFBox** for students and internship candidates to optimize their resumes for Applicant Tracking Systems (ATS), evaluate role compatibility, and receive actionable career guidance.

---

## 1. Project Overview & Features

- **Modern Responsive Web UI**: Clean landing page, intuitive drag-and-drop PDF upload zone, and interactive results dashboard.
- **Apache PDFBox Text Extraction**: Accurately extracts text, headers, and contact info while detecting unreadable scanned image PDFs gracefully without OCR traps.
- **9-Point ATS Compatibility Audit**:
  1. *Text Extractability & Machine Readability* (15 pts)
  2. *Professional Email Address Detection* (10 pts)
  3. *Phone Number Detection* (10 pts)
  4. *Online Professional Presence (LinkedIn / GitHub)* (5 pts)
  5. *Standard Education Section Heading* (15 pts)
  6. *Standard Technical Skills Section Heading* (15 pts)
  7. *Evidence of Projects & Work Experience* (15 pts)
  8. *Optimal Word Count Density (250–1000 words)* (10 pts)
  9. *Text Order & Character Cleanliness* (5 pts)
- **Role Competency & Custom Job Matching**:
  - Predefined roles: **Java Developer**, **Frontend Developer**, **Full Stack Developer**, **Python Developer**, **Data Analyst**, **Software Engineer**.
  - **Custom Role Support**: Specify any custom tech role title.
  - **Custom Job Description Support**: Paste specific employer requirements to cross-check exact keywords.
- **Dual Transparent Scoring**:
  - **ATS Compatibility Score (out of 100)**: Evaluates layout, section headings, and machine-readability.
  - **Role Suitability Score (out of 100)**:
    - *Required Skills Match*: 50 marks
    - *Relevant Projects & Experience*: 30 marks
    - *Qualifications & Certifications*: 20 marks
- **Career Guidance & Recommendations**:
  - *Skills to Learn*: Identifies role competencies not detected in the resume.
  - *Verified Certifications & Courses*: Genuine links to reputable providers (Oracle, Meta, freeCodeCamp, AWS) labeled by credential type and cost.
  - *Portfolio Project Ideas*: Real-world project concepts to bridge skill gaps, complete with tech stacks and STAR-method resume bullet points.
  - *Actionable Resume Writing Tips*: Practical guidelines on action verbs, quantifiable achievements, and ATS formatting formulas.
- **Executive Report Generation**: Downloadable **PDF report** (generated dynamically using Apache PDFBox) and formatted **Text report**.
- **Quick Demo Resumes**: One-click demo resumes built directly into the UI for instant testing.

---

## 2. Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.5 (`spring-boot-starter-web`, `spring-boot-starter-validation`)
- **PDF Processing**: Apache PDFBox 2.0.31
- **Build Tool**: Maven 3.9+
- **Frontend**: HTML5, Modern CSS3 (Flexbox/Grid, CSS Variables), Vanilla JavaScript (ES6+ Fetch API)
- **Icons**: Font Awesome 6.4.0

---

## 3. Project Architecture & Directory Structure

```
ats-resume-analyzer/
├── pom.xml                                   # Maven configuration and dependencies
├── README.md                                 # Complete documentation and setup guide
└── src/
    ├── main/
    │   ├── java/com/ats/resumeanalyzer/
    │   │   ├── ResumeAnalyzerApplication.java# Spring Boot main class
    │   │   ├── config/                       # Application configurations
    │   │   ├── controller/
    │   │   │   ├── ResumeController.java     # PDF upload, analysis, and report download APIs
    │   │   │   └── JobRoleController.java    # Job roles catalog endpoints
    │   │   ├── model/
    │   │   │   ├── AtsCheckItem.java         # ATS rule check definition
    │   │   │   ├── AtsCheckStatus.java       # Status enum (PASSED, WARNING, FAILED)
    │   │   │   ├── CareerRecommendation.java # Guidance package
    │   │   │   ├── CertificationItem.java    # Verified course / cert item
    │   │   │   ├── ContactInfo.java          # Extracted contact details
    │   │   │   ├── ExtractedPdfData.java     # Text & sections parsed from PDF
    │   │   │   ├── JobRole.java              # Role profile & skill requirements
    │   │   │   ├── ProjectRecommendation.java# Portfolio project suggestion
    │   │   │   ├── ResumeAnalysisResult.java # Complete session analysis output
    │   │   │   ├── ResumeProcessingException.java # Friendly error handling
    │   │   │   ├── ResumeSection.java        # Section metadata
    │   │   │   ├── ScoreBreakdown.java       # Dual scoring components
    │   │   │   └── SkillMatchItem.java       # Individual skill match status
    │   │   ├── service/
    │   │   │   ├── AnalysisReportService.java    # PDF & text report generator
    │   │   │   ├── AtsCompatibilityService.java  # 9-point ATS audit engine
    │   │   │   ├── CareerRecommendationService.java # Guidance & tips generator
    │   │   │   ├── JobRoleService.java           # Roles catalog & custom role factory
    │   │   │   ├── PdfTextExtractionService.java # Apache PDFBox parsing & regex
    │   │   │   ├── ResumeAnalysisService.java    # Central analysis orchestrator
    │   │   │   └── SkillMatchingService.java     # Synonym matching & gap detector
    │   │   └── util/
    │   │       └── SampleResumeGenerator.java# Programmatic sample resume PDF creator
    │   └── resources/
    │       ├── application.properties        # Multipart file size (5MB) & port (8080)
    │       └── static/                       # Web frontend
    │           ├── index.html                # Single page web interface
    │           ├── css/
    │           │   └── styles.css            # Responsive modern styling
    │           ├── js/
    │           │   └── app.js                # Frontend client controller
    │           └── sample-resumes/           # Built-in sample test resumes (.pdf)
    └── test/
        └── java/com/ats/resumeanalyzer/
            └── ResumeAnalyzerServiceTest.java# Comprehensive unit & integration tests
```

---

## 4. How to Run the Application

### Prerequisites
- **Java Development Kit (JDK) 17** or higher installed (`java -version`)
- **Apache Maven 3.8+** installed (`mvn -version`)

### Step 1: Open Terminal in the Project Directory
```powershell
cd C:\Users\venne\.gemini\antigravity\scratch\ats-resume-analyzer
```

### Step 2: Build the Project
```powershell
mvn clean package
```

### Step 3: Run the Spring Boot Server
You can run using the Maven Spring Boot plugin:
```powershell
mvn spring-boot:run
```
Or run the packaged fat JAR directly:
```powershell
java -jar target/resume-analyzer-1.0.0.jar
```

### Step 4: Access the Web Application
Open your web browser and navigate to:
```
http://localhost:8080/
```

---

## 5. How to Test and Demonstrate

1. **Quick One-Click Test**:
   - Scroll to the **Upload & Analyze Resume** section.
   - Click the button **"Try Sample Java Developer Resume"**.
   - Notice the PDF is loaded automatically and "Java Developer" is selected.
   - Click **"Analyze Resume Now"**.
   - Observe the analysis animation, dual scores (**100/100 ATS**, **90/100 Role Suitability**), checklist, and recommendations.
2. **Test Sample Resume with Gaps**:
   - Click **"Analyze Another Resume"**.
   - Click **"Try Sample Resume with Gaps"**.
   - Click **"Analyze Resume Now"**.
   - Observe the lower scores (**78/100 ATS**, **28/100 Role Suitability**), identified missing skills (Spring Boot, SQL, Git, etc.), and custom project suggestions targeting those gaps.
3. **Test Custom Role & Job Description**:
   - Select **"-- Custom Job Role --"** from the dropdown.
   - Enter `Cloud Platform Engineer`.
   - In the optional job description box, paste: `We need experience with AWS, Docker, Git, CI/CD, and Microservices.`
   - Click **"Analyze Resume Now"** to see prioritized keyword matching.
4. **Download Report**:
   - Click **"Download PDF Report"** to download the generated PDF summary report.
   - Click **"Download Text Report"** to download the plain-text audit.

---

## 6. Running Unit & Integration Tests

Run the test suite anytime using:
```powershell
mvn test
```
The test suite validates:
- PDFBox extraction on multi-section resumes.
- Contact extraction (Email, Phone, LinkedIn, GitHub).
- 9-point ATS scoring logic and thresholds.
- Skill detection and missing skill identification.
- PDF and Text report generation.

---

## 7. Security and Privacy

- **No Permanent File Storage**: Uploaded PDF resumes are parsed in memory via streams and discarded after the session.
- **Strict Size Limits**: Maximum file size is strictly capped at 5MB.
- **No File Execution**: Resumes are parsed strictly as document data through Apache PDFBox; no native binaries or scripts are executed.
- **XSS Sanitization**: Extracted text is sanitized and HTML-escaped before display.
- **Ethical Evaluation**: Analysis is purely based on technical competencies and structural ATS compliance, excluding demographic or personal characteristics.
