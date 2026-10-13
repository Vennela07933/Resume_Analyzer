package com.ats.resumeanalyzer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Data transfer model holding raw extracted content and metadata from the PDF parser.
 */
public class ExtractedPdfData {
    private String rawText;
    private int pageCount;
    private int wordCount;
    private long fileSizeBytes;
    private String fileName;
    private boolean likelyScanned;
    private ContactInfo contactInfo;
    private Map<String, String> sectionTexts = new HashMap<>();

    public ExtractedPdfData() {}

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public long getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(long fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isLikelyScanned() {
        return likelyScanned;
    }

    public void setLikelyScanned(boolean likelyScanned) {
        this.likelyScanned = likelyScanned;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Map<String, String> getSectionTexts() {
        return sectionTexts;
    }

    public void setSectionTexts(Map<String, String> sectionTexts) {
        this.sectionTexts = sectionTexts;
    }
}
