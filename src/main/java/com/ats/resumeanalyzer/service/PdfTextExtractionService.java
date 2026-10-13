package com.ats.resumeanalyzer.service;

import com.ats.resumeanalyzer.model.ContactInfo;
import com.ats.resumeanalyzer.model.ExtractedPdfData;
import com.ats.resumeanalyzer.model.ResumeProcessingException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service responsible for validating uploaded PDF files, extracting textual content using
 * Apache PDFBox, detecting sections, and parsing candidate contact information.
 */
@Service
public class PdfTextExtractionService {

    private static final Logger logger = LoggerFactory.getLogger(PdfTextExtractionService.class);
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    // Regular expressions for contact information detection
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "(?:(?:\\+|00)?\\d{1,3}[-\\s.]?)?(?:\\(?\\d{2,4}\\)?[-\\s.]?)?\\d{3,5}[-\\s.]?\\d{4,5}"
    );

    private static final Pattern LINKEDIN_PATTERN = Pattern.compile(
            "(?:https?:\\/\\/)?(?:www\\.)?linkedin\\.com\\/in\\/[a-zA-Z0-9_-]+",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern GITHUB_PATTERN = Pattern.compile(
            "(?:https?:\\/\\/)?(?:www\\.)?github\\.com\\/[a-zA-Z0-9_-]+",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern PORTFOLIO_PATTERN = Pattern.compile(
            "(?:https?:\\/\\/)?(?:www\\.)?[a-zA-Z0-9-]+\\.(?:github\\.io|vercel\\.app|netlify\\.app|dev|tech|me|portfolio)[a-zA-Z0-9/_.-]*",
            Pattern.CASE_INSENSITIVE
    );

    // Standard resume section keywords
    private static final Map<String, List<String>> SECTION_KEYWORDS = new LinkedHashMap<>();

    static {
        SECTION_KEYWORDS.put("Education", List.of("education", "academic qualification", "academic background", "academic history", "qualification", "educational qualifications"));
        SECTION_KEYWORDS.put("Skills", List.of("technical skills", "skills", "skills & tools", "core competencies", "technical competencies", "skills & abilities", "technologies", "proficiencies"));
        SECTION_KEYWORDS.put("Experience", List.of("work experience", "experience", "professional experience", "employment history", "internships", "work history", "industrial experience"));
        SECTION_KEYWORDS.put("Projects", List.of("projects", "academic projects", "personal projects", "technical projects", "key projects", "notable projects"));
        SECTION_KEYWORDS.put("Certifications", List.of("certifications", "licenses & certifications", "certificates", "courses & certifications", "credentials", "professional certifications"));
        SECTION_KEYWORDS.put("Achievements", List.of("achievements", "honors", "awards", "honors & awards", "accomplishments", "extracurricular activities", "co-curricular activities", "publications"));
        SECTION_KEYWORDS.put("Summary", List.of("summary", "professional summary", "career objective", "objective", "profile", "about me"));
    }

    /**
     * Extracts text and structured metadata from the given uploaded PDF file.
     *
     * @param file the uploaded MultipartFile
     * @return ExtractedPdfData with parsed content and section maps
     */
    public ExtractedPdfData extractText(MultipartFile file) {
        validateFile(file);

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            if (document.isEncrypted()) {
                throw new ResumeProcessingException("The uploaded PDF is password-protected or encrypted. Please upload an unprotected PDF.");
            }

            int pageCount = document.getNumberOfPages();
            if (pageCount == 0) {
                throw new ResumeProcessingException("The uploaded PDF contains no pages.");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true); // Ensures natural reading order
            String rawExtracted = stripper.getText(document);

            // Sanitize raw text to prevent XSS or non-printable character issues
            String sanitizedText = sanitizeText(rawExtracted);

            ExtractedPdfData result = new ExtractedPdfData();
            result.setFileName(file.getOriginalFilename());
            result.setFileSizeBytes(file.getSize());
            result.setPageCount(pageCount);
            result.setRawText(sanitizedText);

            int wordCount = countWords(sanitizedText);
            result.setWordCount(wordCount);

            // Check if document appears to be a scanned or image-based PDF
            // Heuristic: Less than 50 characters, or less than 15 words per page
            boolean isScanned = sanitizedText.trim().length() < 50 || (wordCount / (double) pageCount) < 15.0;
            result.setLikelyScanned(isScanned);

            // Extract contact info
            ContactInfo contactInfo = extractContactInfo(sanitizedText);
            result.setContactInfo(contactInfo);

            // Segment text into sections
            Map<String, String> sectionTexts = partitionIntoSections(sanitizedText);
            result.setSectionTexts(sectionTexts);

            logger.info("Successfully extracted text from '{}': {} pages, {} words, likelyScanned={}",
                    file.getOriginalFilename(), pageCount, wordCount, isScanned);

            return result;

        } catch (InvalidPasswordException e) {
            throw new ResumeProcessingException("The uploaded PDF is password-protected. Please upload a PDF without password encryption.", e);
        } catch (ResumeProcessingException e) {
            throw e;
        } catch (IOException e) {
            logger.error("IO error reading PDF file: {}", e.getMessage(), e);
            throw new ResumeProcessingException("Failed to read the PDF file. Please ensure it is a valid, uncorrupted PDF document.", e);
        } catch (Exception e) {
            logger.error("Unexpected error during PDF extraction: {}", e.getMessage(), e);
            throw new ResumeProcessingException("An error occurred while parsing your resume: " + e.getMessage(), e);
        }
    }

    /**
     * Validates that the uploaded file exists, has a PDF extension/type, and is within size limits.
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResumeProcessingException("No file was uploaded. Please choose a PDF resume to analyze.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResumeProcessingException("File size exceeds 5MB limit. Please upload a smaller PDF resume.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new ResumeProcessingException("Invalid file format. Only PDF files (.pdf) are supported.");
        }

        String contentType = file.getContentType();
        if (contentType != null && !contentType.equalsIgnoreCase("application/pdf")
                && !contentType.equalsIgnoreCase("application/octet-stream")
                && !contentType.equalsIgnoreCase("application/x-pdf")) {
            throw new ResumeProcessingException("Uploaded file does not appear to be a standard PDF document.");
        }
    }

    /**
     * Sanitizes raw extracted text by removing null bytes and normalizing whitespace.
     */
    public String sanitizeText(String input) {
        if (input == null) {
            return "";
        }
        // Remove null characters and standard control characters except newline and tab
        String cleaned = input.replaceAll("\u0000", "")
                .replaceAll("[\r\n]{3,}", "\n\n")
                .trim();
        return cleaned;
    }

    /**
     * Extracts contact details: name, email, phone number, LinkedIn, GitHub, portfolio.
     */
    public ContactInfo extractContactInfo(String text) {
        ContactInfo info = new ContactInfo();
        if (text == null || text.isBlank()) {
            return info;
        }

        // Email
        Matcher emailMatcher = EMAIL_PATTERN.matcher(text);
        if (emailMatcher.find()) {
            info.setEmail(emailMatcher.group().trim());
        }

        // Phone
        Matcher phoneMatcher = PHONE_PATTERN.matcher(text);
        while (phoneMatcher.find()) {
            String candidatePhone = phoneMatcher.group().replaceAll("[^0-9+]", "");
            // Valid phone typically has 10 to 14 digits
            if (candidatePhone.length() >= 10 && candidatePhone.length() <= 15) {
                info.setPhone(phoneMatcher.group().trim());
                break;
            }
        }

        // LinkedIn
        Matcher linkedInMatcher = LINKEDIN_PATTERN.matcher(text);
        if (linkedInMatcher.find()) {
            info.setLinkedInUrl(linkedInMatcher.group().trim());
        }

        // GitHub
        Matcher githubMatcher = GITHUB_PATTERN.matcher(text);
        if (githubMatcher.find()) {
            info.setGitHubUrl(githubMatcher.group().trim());
        }

        // Portfolio
        Matcher portfolioMatcher = PORTFOLIO_PATTERN.matcher(text);
        if (portfolioMatcher.find()) {
            String found = portfolioMatcher.group().trim();
            // Ensure it's not the already extracted GitHub or LinkedIn
            if (!found.contains("linkedin.com") && !found.contains("github.com")) {
                info.setPortfolioUrl(found);
            }
        }

        // Candidate Name Heuristic: Check first 4 non-empty lines
        String[] lines = text.split("\n");
        for (int i = 0; i < Math.min(lines.length, 5); i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            // Name shouldn't contain email, phone, links, or section headings
            if (!line.contains("@") && !line.toLowerCase().contains("http") && !line.matches(".*\\d{5,}.*")
                    && line.length() > 2 && line.length() < 40 && line.split("\\s+").length <= 4) {
                boolean isHeader = false;
                for (List<String> keywords : SECTION_KEYWORDS.values()) {
                    for (String kw : keywords) {
                        if (line.equalsIgnoreCase(kw)) {
                            isHeader = true;
                            break;
                        }
                    }
                }
                if (!isHeader) {
                    info.setCandidateName(line);
                    break;
                }
            }
        }

        if (info.getCandidateName() == null || info.getCandidateName().isBlank()) {
            info.setCandidateName("Candidate (Name not detected)");
        }

        return info;
    }

    /**
     * Segments text into recognized standard resume sections using heading detection heuristics.
     */
    public Map<String, String> partitionIntoSections(String text) {
        Map<String, String> sections = new HashMap<>();
        if (text == null || text.isBlank()) {
            return sections;
        }

        String[] lines = text.split("\n");
        String currentSection = "Header";
        StringBuilder currentBuffer = new StringBuilder();

        for (String rawLine : lines) {
            String line = rawLine.trim();
            String matchedSection = identifyHeading(line);

            if (matchedSection != null) {
                // Save previous section buffer
                if (currentBuffer.length() > 0) {
                    sections.put(currentSection, currentBuffer.toString().trim());
                }
                currentSection = matchedSection;
                currentBuffer = new StringBuilder();
            } else {
                currentBuffer.append(rawLine).append("\n");
            }
        }

        if (currentBuffer.length() > 0) {
            sections.put(currentSection, currentBuffer.toString().trim());
        }

        return sections;
    }

    /**
     * Checks if a line matches a resume section heading.
     */
    private String identifyHeading(String line) {
        if (line.length() < 3 || line.length() > 40) {
            return null;
        }

        // Strip punctuation like colons, bullet points, hyphens
        String cleaned = line.replaceAll("[:\\-_*#|]", " ").trim().toLowerCase();

        for (Map.Entry<String, List<String>> entry : SECTION_KEYWORDS.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (cleaned.equals(keyword) || cleaned.equals(keyword + "s")) {
                    return entry.getKey();
                }
            }
        }

        return null;
    }

    /**
     * Helper to count words in a string.
     */
    private int countWords(String text) {
        if (text == null || text.isBlank()) return 0;
        String[] words = text.trim().split("\\s+");
        return words.length;
    }
}
