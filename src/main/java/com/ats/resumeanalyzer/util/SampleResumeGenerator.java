package com.ats.resumeanalyzer.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

/**
 * Utility to generate realistic sample resumes in PDF format for demonstration and testing.
 */
public class SampleResumeGenerator {

    public static void generateSampleJavaResume(File outputFile) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float x = 50;
                float y = 780;
                float leading = 13;

                // Name & Header
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
                cs.newLineAtOffset(x, y);
                cs.showText("RAHUL SHARMA");
                cs.endText();
                y -= 20;

                // Contact Details
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(x, y);
                cs.showText("rahul.sharma@example.com | +91 9876543210 | Bengaluru, India");
                cs.endText();
                y -= 14;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(x, y);
                cs.showText("LinkedIn: linkedin.com/in/rahul-sharma-dev | GitHub: github.com/rahul-sharma-dev");
                cs.endText();
                y -= 22;

                // Professional Summary
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("PROFESSIONAL SUMMARY");
                cs.endText();
                y -= 15;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("Motivated Computer Science graduate with hands-on experience in Java, Spring Boot, REST APIs,");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("and relational database design. Passionate about building robust backend microservices.");
                cs.endText();
                y -= 20;

                // Education
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("EDUCATION");
                cs.endText();
                y -= 15;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(x, y);
                cs.showText("Bachelor of Technology in Computer Science & Engineering (2020 - 2024)");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("National Institute of Technology | CGPA: 8.6 / 10.0");
                cs.endText();
                y -= 20;

                // Technical Skills
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("TECHNICAL SKILLS");
                cs.endText();
                y -= 15;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("Programming Languages: Java (Java 17, Core Java, Collections Framework, Multithreading, OOP)");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("Frameworks & Tools: Spring Boot, Spring MVC, Hibernate, REST APIs, Maven, JUnit, Git");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("Databases: SQL, MySQL, PostgreSQL, Relational Database Modeling");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("Concepts: Data Structures & Algorithms, Exception Handling, Object-Oriented Design");
                cs.endText();
                y -= 20;

                // Projects
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("PROJECTS");
                cs.endText();
                y -= 15;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(x, y);
                cs.showText("E-Commerce Order Management API | Spring Boot, MySQL, Hibernate, REST APIs");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("- Engineered 15+ REST endpoints in Spring Boot with transactional consistency and data validation.");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("- Designed relational database schema in MySQL utilizing Hibernate JPA for optimized query retrieval.");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("- Implemented custom exception handling and achieved 85%+ test coverage using JUnit 5 and Mockito.");
                cs.endText();
                y -= 16;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(x, y);
                cs.showText("Library Management System | Core Java, Collections Framework, SQL, Git");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("- Developed desktop and console application applying Object-Oriented Programming (OOP) principles.");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("- Used Java Collections Framework (ArrayList, HashMap) to manage in-memory book and patron states.");
                cs.endText();
                y -= 20;

                // Work Experience
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("EXPERIENCE");
                cs.endText();
                y -= 15;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(x, y);
                cs.showText("Software Engineering Intern | TechCorp Solutions (Jan 2024 - Jun 2024)");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("- Built RESTful microservice components using Spring Boot and PostgreSQL, decreasing API latency by 20%.");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("- Collaborated in an Agile Scrum team using Git for version control and participating in peer code reviews.");
                cs.endText();
                y -= 20;

                // Certifications
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("CERTIFICATIONS");
                cs.endText();
                y -= 15;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("- Oracle Certified Associate: Java SE Programmer (Oracle, 2023)");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("- Java Development Course with Spring Boot (freeCodeCamp, 2023)");
                cs.endText();
            }

            doc.save(outputFile);
        }
    }

    public static void generateSampleJuniorResume(File outputFile) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float x = 50;
                float y = 780;
                float leading = 14;

                // Minimal Header without LinkedIn or Phone
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 16);
                cs.newLineAtOffset(x, y);
                cs.showText("ANITA VERMA");
                cs.endText();
                y -= 18;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 10);
                cs.newLineAtOffset(x, y);
                cs.showText("Email: anita.verma@example.com");
                cs.endText();
                y -= 25;

                // Objective
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("CAREER OBJECTIVE");
                cs.endText();
                y -= 16;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("Seeking an entry level developer position in software development where I can apply my skills.");
                cs.endText();
                y -= 25;

                // Education (without clear CGPA or date)
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("EDUCATION");
                cs.endText();
                y -= 16;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("B.Tech in Computer Science, City Engineering College");
                cs.endText();
                y -= 25;

                // Skills (few skills, missing Spring Boot, SQL, Docker, etc.)
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("SKILLS");
                cs.endText();
                y -= 16;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("Java, HTML, CSS, Problem Solving");
                cs.endText();
                y -= 25;

                // Projects (Brief, no metrics, missing Git/DB details)
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                cs.newLineAtOffset(x, y);
                cs.showText("PROJECTS");
                cs.endText();
                y -= 16;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(x, y);
                cs.showText("Calculator Web Page");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("Created a calculator website using HTML and CSS for performing basic arithmetic operations.");
                cs.endText();
                y -= 25;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 10);
                cs.newLineAtOffset(x, y);
                cs.showText("Java Console Quiz App");
                cs.endText();
                y -= leading;

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 9);
                cs.newLineAtOffset(x, y);
                cs.showText("Built a simple multiple choice quiz program in Java using OOP and loops.");
                cs.endText();
            }

            doc.save(outputFile);
        }
    }
}
