/**
 * ATS-Friendly Resume Analyzer & Career Guidance System
 * Frontend Client Controller
 */

document.addEventListener('DOMContentLoaded', () => {
    // Application State
    let selectedFile = null;
    let currentAnalysis = null;

    // DOM Elements
    const dropzone = document.getElementById('dropzone');
    const fileInput = document.getElementById('resumeFileInput');
    const browseBtn = document.getElementById('browseBtn');
    const dropzonePrompt = document.getElementById('dropzonePrompt');
    const fileInfoBox = document.getElementById('fileInfoBox');
    const fileNameLabel = document.getElementById('fileNameLabel');
    const fileSizeLabel = document.getElementById('fileSizeLabel');
    const removeFileBtn = document.getElementById('removeFileBtn');

    const roleSelect = document.getElementById('roleSelect');
    const customRoleGroup = document.getElementById('customRoleGroup');
    const customRoleInput = document.getElementById('customRoleInput');
    const jobDescTextarea = document.getElementById('jobDescTextarea');

    const resumeUploadForm = document.getElementById('resumeUploadForm');
    const analyzeSubmitBtn = document.getElementById('analyzeSubmitBtn');
    const uploadErrorBanner = document.getElementById('uploadErrorBanner');
    const errorMessageText = document.getElementById('errorMessageText');

    const loadingCard = document.getElementById('loadingCard');
    const loadingStatusText = document.getElementById('loadingStatusText');
    const loadingSubtext = document.getElementById('loadingSubtext');
    const analysisProgressBar = document.getElementById('analysisProgressBar');

    const resultsSection = document.getElementById('results');
    const loadSampleJavaBtn = document.getElementById('loadSampleJavaBtn');
    const loadSampleJuniorBtn = document.getElementById('loadSampleJuniorBtn');

    const downloadPdfReportBtn = document.getElementById('downloadPdfReportBtn');
    const downloadTextReportBtn = document.getElementById('downloadTextReportBtn');
    const analyzeAnotherBtn = document.getElementById('analyzeAnotherBtn');

    // --------------------------------------------------------------------------
    // 1. Role Selection & Custom Role Visibility
    // --------------------------------------------------------------------------
    roleSelect.addEventListener('change', () => {
        if (roleSelect.value === 'custom') {
            customRoleGroup.classList.remove('hidden');
            customRoleInput.setAttribute('required', 'true');
            customRoleInput.focus();
        } else {
            customRoleGroup.classList.add('hidden');
            customRoleInput.removeAttribute('required');
            customRoleInput.value = '';
        }
    });

    // --------------------------------------------------------------------------
    // 2. Drag & Drop File Handling
    // --------------------------------------------------------------------------
    browseBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        fileInput.click();
    });

    dropzone.addEventListener('click', () => {
        if (!selectedFile) {
            fileInput.click();
        }
    });

    dropzone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropzone.classList.add('dragover');
    });

    dropzone.addEventListener('dragleave', () => {
        dropzone.classList.remove('dragover');
    });

    dropzone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropzone.classList.remove('dragover');
        if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
            handleSelectedFile(e.dataTransfer.files[0]);
        }
    });

    fileInput.addEventListener('change', () => {
        if (fileInput.files && fileInput.files.length > 0) {
            handleSelectedFile(fileInput.files[0]);
        }
    });

    removeFileBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        clearSelectedFile();
    });

    function handleSelectedFile(file) {
        hideError();

        // Validation 1: Format
        if (!file.name.toLowerCase().endsWith('.pdf') && file.type !== 'application/pdf') {
            showError('Invalid file type. Please upload a PDF resume document (.pdf).');
            return;
        }

        // Validation 2: Size (Max 5MB)
        const maxBytes = 5 * 1024 * 1024;
        if (file.size > maxBytes) {
            showError('File exceeds 5MB limit. Please upload a smaller PDF file.');
            return;
        }

        selectedFile = file;
        fileNameLabel.textContent = file.name;
        fileSizeLabel.textContent = formatBytes(file.size);

        dropzonePrompt.classList.add('hidden');
        fileInfoBox.classList.remove('hidden');
    }

    function clearSelectedFile() {
        selectedFile = null;
        fileInput.value = '';
        dropzonePrompt.classList.remove('hidden');
        fileInfoBox.classList.add('hidden');
        hideError();
    }

    // --------------------------------------------------------------------------
    // 3. Quick Sample Resumes Loader
    // --------------------------------------------------------------------------
    loadSampleJavaBtn.addEventListener('click', async () => {
        await loadSampleFile(
            'sample-resumes/Sample_Java_Developer_Resume.pdf',
            'Sample_Java_Developer_Resume.pdf',
            'java-developer'
        );
    });

    loadSampleJuniorBtn.addEventListener('click', async () => {
        await loadSampleFile(
            'sample-resumes/Sample_Junior_Resume_With_Gaps.pdf',
            'Sample_Junior_Resume_With_Gaps.pdf',
            'java-developer'
        );
    });

    async function loadSampleFile(url, fileName, defaultRoleId) {
        try {
            hideError();
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error('Failed to load sample resume.');
            }
            const blob = await response.blob();
            const file = new File([blob], fileName, { type: 'application/pdf' });
            handleSelectedFile(file);

            roleSelect.value = defaultRoleId;
            roleSelect.dispatchEvent(new Event('change'));

            // Smooth scroll to analyzer
            document.getElementById('analyzer').scrollIntoView({ behavior: 'smooth' });
        } catch (err) {
            showError('Could not load sample resume: ' + err.message);
        }
    }

    // --------------------------------------------------------------------------
    // 4. Form Submission & Analysis Execution
    // --------------------------------------------------------------------------
    resumeUploadForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        hideError();

        if (!selectedFile) {
            showError('Please choose or drag-and-drop a PDF resume to analyze.');
            return;
        }

        const roleId = roleSelect.value;
        const customRole = customRoleInput.value.trim();
        const jobDesc = jobDescTextarea.value.trim();

        if (roleId === 'custom' && !customRole) {
            showError('Please specify your custom job title.');
            customRoleInput.focus();
            return;
        }

        // Show loading state
        showLoadingState(true);

        const formData = new FormData();
        formData.append('file', selectedFile);
        formData.append('roleId', roleId);
        if (customRole) {
            formData.append('customRole', customRole);
        }
        if (jobDesc) {
            formData.append('jobDescription', jobDesc);
        }

        try {
            // Stage animations for realistic feedback
            simulateProgressSteps();

            const response = await fetch('/api/resume/analyze', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                const errData = await response.json().catch(() => null);
                const msg = errData && errData.error ? errData.error : 'Analysis failed (HTTP ' + response.status + ')';
                throw new Error(msg);
            }

            const result = await response.json();
            currentAnalysis = result;

            // Render results
            renderResults(result);

            // Hide loading, show results
            showLoadingState(false);
            resultsSection.classList.remove('hidden');

            // Scroll to results
            resultsSection.scrollIntoView({ behavior: 'smooth' });

        } catch (error) {
            showLoadingState(false);
            showError(error.message);
        }
    });

    function simulateProgressSteps() {
        analysisProgressBar.style.width = '20%';
        loadingStatusText.textContent = 'Extracting resume text...';
        loadingSubtext.textContent = 'Parsing document structure with Apache PDFBox...';

        setTimeout(() => {
            analysisProgressBar.style.width = '50%';
            loadingStatusText.textContent = 'Auditing ATS compliance...';
            loadingSubtext.textContent = 'Running 9-point rule-based structural checks...';
        }, 600);

        setTimeout(() => {
            analysisProgressBar.style.width = '80%';
            loadingStatusText.textContent = 'Matching skills & role competencies...';
            loadingSubtext.textContent = 'Evaluating skill gaps and computing suitability marks...';
        }, 1200);
    }

    function showLoadingState(isLoading) {
        if (isLoading) {
            loadingCard.classList.remove('hidden');
            analyzeSubmitBtn.disabled = true;
            analyzeSubmitBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Analyzing...';
        } else {
            loadingCard.classList.add('hidden');
            analyzeSubmitBtn.disabled = false;
            analyzeSubmitBtn.innerHTML = '<i class="fa-solid fa-magnifying-glass-chart"></i> Analyze Resume Now';
            analysisProgressBar.style.width = '100%';
        }
    }

    function showError(msg) {
        errorMessageText.textContent = msg;
        uploadErrorBanner.classList.remove('hidden');
    }

    function hideError() {
        uploadErrorBanner.classList.add('hidden');
    }

    // --------------------------------------------------------------------------
    // 5. Results Rendering
    // --------------------------------------------------------------------------
    function renderResults(res) {
        // Candidate Metadata
        document.getElementById('resTargetRolePill').textContent = res.targetJobRole + (res.customRoleUsed ? ' (Custom)' : '');
        document.getElementById('resCandidateName').textContent = res.contactInfo.candidateName || 'Candidate';
        document.getElementById('resFileName').textContent = res.fileName;
        document.getElementById('resWordCount').textContent = res.totalWordCount;
        document.getElementById('resPageCount').textContent = res.pageCount;
        document.getElementById('resTimestamp').textContent = res.analysisTimestamp || 'Just now';

        // Scanned Warning
        const scannedBox = document.getElementById('scannedWarningBox');
        if (res.scannedPdfWarning) {
            scannedBox.classList.remove('hidden');
        } else {
            scannedBox.classList.add('hidden');
        }

        // ATS Compatibility Score
        const atsScore = res.scores.atsCompatibilityTotal;
        document.getElementById('atsScoreValue').textContent = atsScore;
        const atsStatusBadge = document.getElementById('atsStatusBadge');
        const atsRadial = document.getElementById('atsScoreRadial');

        applyScoreStyling(atsScore, atsStatusBadge, atsRadial);
        document.getElementById('atsScoreExplanationText').textContent = res.scores.atsScoreExplanation;

        // Role Suitability Score
        const roleScore = res.scores.roleSuitabilityTotal;
        document.getElementById('roleScoreValue').textContent = roleScore;
        const roleStatusBadge = document.getElementById('roleStatusBadge');
        const roleRadial = document.getElementById('roleScoreRadial');

        applyScoreStyling(roleScore, roleStatusBadge, roleRadial);

        // Breakdown bars
        const skillsScore = res.scores.skillsScore;
        const projScore = res.scores.projectsAndExperienceScore;
        const qualScore = res.scores.qualificationsAndCertsScore;

        document.getElementById('barSkillsText').textContent = `${skillsScore} / 50`;
        document.getElementById('barSkillsFill').style.width = `${(skillsScore / 50) * 100}%`;

        document.getElementById('barProjectsText').textContent = `${projScore} / 30`;
        document.getElementById('barProjectsFill').style.width = `${(projScore / 30) * 100}%`;

        document.getElementById('barQualText').textContent = `${qualScore} / 20`;
        document.getElementById('barQualFill').style.width = `${(qualScore / 20) * 100}%`;

        // Render Contact Info
        renderContactInfo(res.contactInfo);

        // Render Resume Sections
        renderSections(res.sections);

        // Render Skills
        renderSkills(res);

        // Render ATS Checklist
        renderAtsChecklist(res.atsChecklist);

        // Render Career Guidance
        renderCareerGuidance(res.recommendations);
    }

    function applyScoreStyling(score, badgeElem, circleElem) {
        badgeElem.className = 'score-badge-status';
        let color = '#2563eb';

        if (score >= 85) {
            badgeElem.textContent = 'Excellent';
            badgeElem.classList.add('status-excellent');
            color = '#10b981';
        } else if (score >= 70) {
            badgeElem.textContent = 'Good';
            badgeElem.classList.add('status-good');
            color = '#2563eb';
        } else if (score >= 50) {
            badgeElem.textContent = 'Needs Work';
            badgeElem.classList.add('status-warning');
            color = '#f59e0b';
        } else {
            badgeElem.textContent = 'Critical Gaps';
            badgeElem.classList.add('status-poor');
            color = '#ef4444';
        }

        circleElem.style.borderColor = color;
    }

    function renderContactInfo(contact) {
        const list = document.getElementById('contactInfoList');
        list.innerHTML = '';

        const items = [
            {
                icon: 'fa-envelope',
                label: 'Email',
                value: contact.email,
                isLink: false,
                found: contact.emailFound
            },
            {
                icon: 'fa-phone',
                label: 'Phone',
                value: contact.phone,
                isLink: false,
                found: contact.phoneFound
            },
            {
                icon: 'fa-brands fa-linkedin',
                label: 'LinkedIn',
                value: contact.linkedInUrl,
                isLink: true,
                found: !!contact.linkedInUrl
            },
            {
                icon: 'fa-brands fa-github',
                label: 'GitHub',
                value: contact.gitHubUrl,
                isLink: true,
                found: !!contact.gitHubUrl
            },
            {
                icon: 'fa-globe',
                label: 'Portfolio',
                value: contact.portfolioUrl,
                isLink: true,
                found: !!contact.portfolioUrl
            }
        ];

        items.forEach(item => {
            const li = document.createElement('li');
            const iconHtml = `<i class="fa-solid ${item.icon}"></i>`;
            if (item.found && item.value) {
                const valDisplay = item.isLink 
                    ? `<a href="${item.value.startsWith('http') ? item.value : 'https://' + item.value}" target="_blank" rel="noopener noreferrer">${item.value}</a>`
                    : `<span>${item.value}</span>`;
                li.innerHTML = `${iconHtml} <strong>${item.label}:</strong> ${valDisplay}`;
            } else {
                li.innerHTML = `${iconHtml} <strong>${item.label}:</strong> <span class="text-warning">Not detected in resume</span>`;
            }
            list.appendChild(li);
        });
    }

    function renderSections(sections) {
        const container = document.getElementById('detectedSectionsContainer');
        container.innerHTML = '';

        sections.forEach(sec => {
            const div = document.createElement('div');
            div.className = `section-pill-item ${sec.detected ? 'detected' : 'missing'}`;
            div.innerHTML = `
                <span class="sec-name">
                    <i class="fa-solid ${sec.detected ? 'fa-circle-check' : 'fa-circle-xmark'}"></i> ${sec.name}
                </span>
                <span class="sec-words">${sec.detected ? sec.wordCount + ' words' : 'Missing'}</span>
            `;
            container.appendChild(div);
        });
    }

    function renderSkills(res) {
        // Matched Skills
        const matchedContainer = document.getElementById('matchedSkillsContainer');
        matchedContainer.innerHTML = '';
        document.getElementById('matchedSkillsCount').textContent = res.matchedSkills.length;

        if (res.matchedSkills.length === 0) {
            matchedContainer.innerHTML = '<span class="text-muted">No direct required role skills detected. Review suggestions below.</span>';
        } else {
            res.matchedSkills.forEach(skill => {
                const span = document.createElement('span');
                span.className = 'skill-badge skill-badge-matched';
                span.innerHTML = `<i class="fa-solid fa-check"></i> ${escapeHtml(skill)}`;
                matchedContainer.appendChild(span);
            });
        }

        // Skills Not Detected
        const missingContainer = document.getElementById('missingSkillsContainer');
        missingContainer.innerHTML = '';
        document.getElementById('missingSkillsCount').textContent = res.missingSkills.length;

        if (res.missingSkills.length === 0) {
            missingContainer.innerHTML = '<span class="text-success"><i class="fa-solid fa-circle-check"></i> Excellent! All target role skills were detected in your resume.</span>';
        } else {
            res.missingSkills.forEach(skill => {
                const span = document.createElement('span');
                span.className = 'skill-badge skill-badge-missing';
                span.innerHTML = `<i class="fa-solid fa-circle-question"></i> ${escapeHtml(skill)}`;
                missingContainer.appendChild(span);
            });
        }

        // All Detected Skills
        const allContainer = document.getElementById('allDetectedSkillsContainer');
        allContainer.innerHTML = '';
        document.getElementById('allSkillsCount').textContent = res.allDetectedSkills.length;

        if (res.allDetectedSkills.length === 0) {
            allContainer.innerHTML = '<span class="text-muted">No recognized technical skills found in resume text.</span>';
        } else {
            res.allDetectedSkills.forEach(skill => {
                const span = document.createElement('span');
                span.className = 'skill-badge skill-badge-neutral';
                span.textContent = skill;
                allContainer.appendChild(span);
            });
        }
    }

    function renderAtsChecklist(checklist) {
        const container = document.getElementById('atsChecklistContainer');
        container.innerHTML = '';

        checklist.forEach(item => {
            const card = document.createElement('div');
            const statusClass = item.status.toLowerCase();
            card.className = `check-item-card ${statusClass}`;

            let statusIcon = 'fa-check';
            if (item.status === 'WARNING') statusIcon = 'fa-triangle-exclamation';
            if (item.status === 'FAILED') statusIcon = 'fa-xmark';

            card.innerHTML = `
                <div class="check-item-header">
                    <h4><i class="fa-solid ${statusIcon}"></i> ${escapeHtml(item.ruleName)}</h4>
                    <span class="check-badge badge-${statusClass}">
                        ${item.status} (${item.scoreEarned}/${item.weight} pts)
                    </span>
                </div>
                <p class="check-item-details">${escapeHtml(item.details)}</p>
                <div class="check-item-suggestion">
                    <i class="fa-solid fa-lightbulb"></i> <strong>ATS Tip:</strong> ${escapeHtml(item.recommendation)}
                </div>
            `;
            container.appendChild(card);
        });
    }

    function renderCareerGuidance(recommendations) {
        // Projects
        const projectsContainer = document.getElementById('recommendedProjectsContainer');
        projectsContainer.innerHTML = '';

        recommendations.recommendedProjects.forEach(proj => {
            const card = document.createElement('div');
            card.className = 'project-card';

            const techTags = proj.keyTechnologies.map(t => `<span class="tech-tag">${escapeHtml(t)}</span>`).join('');

            card.innerHTML = `
                <div class="project-header">
                    <h4>${escapeHtml(proj.title)}</h4>
                    <span class="complexity-pill">${escapeHtml(proj.complexity)}</span>
                </div>
                <p class="project-desc">${escapeHtml(proj.description)}</p>
                <div class="project-stack">${techTags}</div>
                <div class="project-bullet-tip">
                    <strong>Suggested Resume Bullet (STAR Method):</strong>
                    "${escapeHtml(proj.suggestedResumeBullet)}"
                </div>
            `;
            projectsContainer.appendChild(card);
        });

        // Certifications & Verified Courses
        const certsContainer = document.getElementById('recommendedCertsContainer');
        certsContainer.innerHTML = '';

        recommendations.recommendedCertifications.forEach(cert => {
            const card = document.createElement('div');
            card.className = 'cert-card';

            card.innerHTML = `
                <div class="cert-badges">
                    <span class="badge-cert-type">${escapeHtml(cert.credentialType)}</span>
                    <span class="badge-cost">${escapeHtml(cert.costCategory)}</span>
                </div>
                <h4>${escapeHtml(cert.title)}</h4>
                <div class="cert-provider"><i class="fa-solid fa-building-columns"></i> ${escapeHtml(cert.provider)}</div>
                <p class="cert-relevance">${escapeHtml(cert.relevance)}</p>
                <a href="${cert.officialUrl}" target="_blank" rel="noopener noreferrer" class="btn-cert-link">
                    Explore Course / Exam <i class="fa-solid fa-arrow-up-right-from-square"></i>
                </a>
            `;
            certsContainer.appendChild(card);
        });

        // Resume Improvement Suggestions
        const suggestionsList = document.getElementById('resumeSuggestionsList');
        suggestionsList.innerHTML = '';

        recommendations.resumeImprovementSuggestions.forEach(tip => {
            const li = document.createElement('li');
            li.innerHTML = `<i class="fa-solid fa-circle-check"></i> <div>${escapeHtml(tip)}</div>`;
            suggestionsList.appendChild(li);
        });
    }

    // --------------------------------------------------------------------------
    // 6. Tabs Management
    // --------------------------------------------------------------------------
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabPanes = document.querySelectorAll('.tab-pane');

    tabButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const targetTab = btn.getAttribute('data-tab');

            tabButtons.forEach(b => b.classList.remove('active'));
            tabPanes.forEach(p => p.classList.remove('active'));

            btn.classList.add('active');
            const targetPane = document.getElementById(targetTab);
            if (targetPane) {
                targetPane.classList.add('active');
            }
        });
    });

    // --------------------------------------------------------------------------
    // 7. Report Download Actions
    // --------------------------------------------------------------------------
    downloadPdfReportBtn.addEventListener('click', () => {
        if (!currentAnalysis) return;
        window.location.href = `/api/resume/report/${currentAnalysis.id}/pdf`;
    });

    downloadTextReportBtn.addEventListener('click', () => {
        if (!currentAnalysis) return;
        window.location.href = `/api/resume/report/${currentAnalysis.id}/text`;
    });

    analyzeAnotherBtn.addEventListener('click', () => {
        resultsSection.classList.add('hidden');
        clearSelectedFile();
        document.getElementById('analyzer').scrollIntoView({ behavior: 'smooth' });
    });

    // --------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------
    function formatBytes(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
    }

    function escapeHtml(text) {
        if (!text) return '';
        const map = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;',
            '"': '&quot;',
            "'": '&#039;'
        };
        return text.replace(/[&<>"']/g, m => map[m]);
    }
});
