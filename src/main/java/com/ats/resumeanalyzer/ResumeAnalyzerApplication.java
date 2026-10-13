package com.ats.resumeanalyzer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main entry point for the ATS-Friendly Resume Analyzer and Career Guidance System.
 * 
 * Developed with Spring Boot, Java 17, and Apache PDFBox.
 */
@SpringBootApplication
public class ResumeAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResumeAnalyzerApplication.class, args);
    }

    @Bean
    public CommandLineRunner printReadyBanner() {
        return args -> {
            System.out.println("\n" + "=".repeat(72));
            System.out.println("   >>> ATS RESUME ANALYZER IS LIVE AND READY! <<<");
            System.out.println("   Click or open the link below in your browser:");
            System.out.println("   http://localhost:8080/");
            System.out.println("=".repeat(72) + "\n");
        };
    }
}

