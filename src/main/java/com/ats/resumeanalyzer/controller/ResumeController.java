package com.ats.resumeanalyzer.controller;

import com.ats.resumeanalyzer.model.ResumeAnalysisResult;
import com.ats.resumeanalyzer.model.ResumeProcessingException;
import com.ats.resumeanalyzer.service.AnalysisReportService;
import com.ats.resumeanalyzer.service.ResumeAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Controller exposing endpoints for uploading PDF resumes, analyzing content,
 * and downloading comprehensive assessment reports.
 */
@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
public class ResumeController {

    private static final Logger logger = LoggerFactory.getLogger(ResumeController.class);

    private final ResumeAnalysisService resumeAnalysisService;
    private final AnalysisReportService analysisReportService;

    public ResumeController(ResumeAnalysisService resumeAnalysisService, AnalysisReportService analysisReportService) {
        this.resumeAnalysisService = resumeAnalysisService;
        this.analysisReportService = analysisReportService;
    }

    /**
     * Accepts a PDF resume file and target job role parameters, executing full ATS and skill analysis.
     */
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> analyzeResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "roleId", defaultValue = "java-developer") String roleId,
            @RequestParam(value = "customRole", required = false) String customRole,
            @RequestParam(value = "jobDescription", required = false) String jobDescription) {

        logger.info("Received resume upload request: file='{}' ({} bytes), roleId='{}'",
                file != null ? file.getOriginalFilename() : "null",
                file != null ? file.getSize() : 0,
                roleId);

        try {
            ResumeAnalysisResult result = resumeAnalysisService.analyzeResume(file, roleId, customRole, jobDescription);
            return ResponseEntity.ok(result);
        } catch (ResumeProcessingException e) {
            logger.warn("Resume processing warning: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Internal error during resume analysis: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred during resume analysis: " + e.getMessage()));
        }
    }

    /**
     * Retrieves stored analysis result data by session id.
     */
    @GetMapping("/result/{analysisId}")
    public ResponseEntity<?> getResult(@PathVariable String analysisId) {
        ResumeAnalysisResult result = resumeAnalysisService.getAnalysisResult(analysisId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Analysis report not found or session expired."));
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Generates and downloads the PDF analysis report.
     */
    @GetMapping("/report/{analysisId}/pdf")
    public ResponseEntity<?> downloadPdfReport(@PathVariable String analysisId) {
        ResumeAnalysisResult result = resumeAnalysisService.getAnalysisResult(analysisId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Analysis session not found. Please analyze your resume again."));
        }

        try {
            byte[] pdfBytes = analysisReportService.generatePdfReport(result);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = "ATS_Resume_Report_" + result.getTargetJobRole().replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            logger.error("Error generating PDF report: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Could not generate PDF report: " + e.getMessage()));
        }
    }

    /**
     * Generates and downloads the plain-text analysis report.
     */
    @GetMapping("/report/{analysisId}/text")
    public ResponseEntity<?> downloadTextReport(@PathVariable String analysisId) {
        ResumeAnalysisResult result = resumeAnalysisService.getAnalysisResult(analysisId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Analysis session not found. Please analyze your resume again."));
        }

        String textReport = analysisReportService.generateTextReport(result);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        String filename = "ATS_Resume_Report_" + result.getTargetJobRole().replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        headers.setContentDispositionFormData("attachment", filename);

        return new ResponseEntity<>(textReport.getBytes(), headers, HttpStatus.OK);
    }

    /**
     * Exception handler for file upload size limits.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("error", "The uploaded file is too large. Maximum supported PDF file size is 5MB."));
    }
}
