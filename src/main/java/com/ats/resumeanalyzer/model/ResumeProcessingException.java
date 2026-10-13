package com.ats.resumeanalyzer.model;

/**
 * Custom runtime exception for user-friendly error messages during resume processing.
 */
public class ResumeProcessingException extends RuntimeException {
    public ResumeProcessingException(String message) {
        super(message);
    }

    public ResumeProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
