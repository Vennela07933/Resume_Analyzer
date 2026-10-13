package com.ats.resumeanalyzer.service;

import com.ats.resumeanalyzer.model.CertificationItem;
import com.ats.resumeanalyzer.model.JobRole;
import com.ats.resumeanalyzer.model.ProjectRecommendation;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;

/**
 * Service managing job roles, required skill profiles, verified certifications,
 * and portfolio project suggestions.
 */
@Service
public class JobRoleService {

    private final Map<String, JobRole> roleRegistry = new LinkedHashMap<>();

    @PostConstruct
    public void initRoles() {
        // 1. Java Developer
        JobRole javaRole = new JobRole(
                "java-developer",
                "Java Developer",
                "Focuses on server-side development, RESTful APIs, relational databases, and enterprise Java ecosystems.",
                List.of("Core Java", "OOP", "Collections Framework", "Spring Boot", "REST APIs", "SQL", "Git", "Exception Handling"),
                List.of("Hibernate", "Spring Data JPA", "Maven", "JUnit", "Microservices", "Docker", "Multithreading", "PostgreSQL", "MySQL")
        );
        javaRole.setRecommendedCertifications(List.of(
                new CertificationItem(
                        "Oracle Certified Associate: Java SE Programmer",
                        "Oracle",
                        "Professional Industry Certification",
                        "Paid Exam",
                        "https://education.oracle.com/oracle-certified-associate-java-se-8-programmer/pexam_1Z0-808",
                        "Validates core Java syntax, OOP design principles, and exception handling fundamentals."
                ),
                new CertificationItem(
                        "Spring Certified Professional",
                        "Broadcom / Spring",
                        "Professional Industry Certification",
                        "Paid Exam",
                        "https://spring.io/learn",
                        "Recognized benchmark for building enterprise Spring Boot microservices and RESTful applications."
                ),
                new CertificationItem(
                        "Java Programming Course",
                        "freeCodeCamp",
                        "Free Verified Learning",
                        "Free",
                        "https://www.freecodecamp.org/news/tag/java/",
                        "Free self-paced hands-on tutorials covering Java basics, data structures, and algorithms."
                )
        ));
        javaRole.setRecommendedProjects(List.of(
                new ProjectRecommendation(
                        "RESTful E-Commerce Inventory & Order API",
                        "Develop a Spring Boot RESTful API handling products, cart management, and order placement with relational database persistence.",
                        "Beginner-Friendly",
                        List.of("Java 17", "Spring Boot", "Spring Data JPA", "MySQL / H2", "Swagger/OpenAPI"),
                        List.of("REST architectural principles", "Database entity relationships (@OneToMany, @ManyToOne)", "Input validation with Bean Validation"),
                        "Engineered 12+ REST endpoints in Spring Boot with Spring Data JPA, ensuring ACID transactions and automated Swagger documentation."
                ),
                new ProjectRecommendation(
                        "Student & Course Portal with Spring MVC & Security",
                        "Build a web portal for student course registration featuring role-based access control (Student vs Admin) and automated email confirmation.",
                        "Intermediate",
                        List.of("Spring Boot", "Spring Security", "Thymeleaf / REST", "PostgreSQL", "JUnit 5"),
                        List.of("Authentication & Authorization flows", "Database indexing and migrations", "Unit and integration testing"),
                        "Implemented secure role-based authentication and course enrollment workflow covered with 85%+ JUnit test coverage."
                ),
                new ProjectRecommendation(
                        "Banking Transaction & Audit Microservice",
                        "Construct a high-reliability transaction service simulating money transfers with custom exception handling, idempotency, and logging.",
                        "Intermediate",
                        List.of("Java", "Spring Boot", "Docker", "REST APIs", "Maven"),
                        List.of("Concurrency safety", "Custom exception hierarchy", "Containerization with Docker"),
                        "Developed microservice processing financial transfer requests with idempotent transaction records and Docker containerization."
                )
        ));
        javaRole.setGeneralTips(List.of(
                "Emphasize Object-Oriented Design (SOLID principles) and design patterns in project descriptions.",
                "Mention your testing practices, such as JUnit or Mockito test suites.",
                "Detail how you managed relational schemas (normalization, indexes, JPA annotations)."
        ));
        roleRegistry.put(javaRole.getId(), javaRole);

        // 2. Frontend Developer
        JobRole frontendRole = new JobRole(
                "frontend-developer",
                "Frontend Developer",
                "Specializes in user interfaces, component-driven UI architecture, state management, and responsive web experiences.",
                List.of("HTML5", "CSS3", "JavaScript", "React", "Responsive Design", "Git", "REST APIs"),
                List.of("TypeScript", "Tailwind CSS", "Redux", "Next.js", "Web Performance", "Vite", "Jest")
        );
        frontendRole.setRecommendedCertifications(List.of(
                new CertificationItem(
                        "Meta Front-End Developer Professional Certificate",
                        "Meta on Coursera",
                        "Professional Certificate",
                        "Free Audit / Paid Certificate",
                        "https://www.coursera.org/professional-certificates/meta-front-end-developer",
                        "Comprehensive industry certificate covering React, modern JavaScript, UI/UX basics, and version control."
                ),
                new CertificationItem(
                        "Responsive Web Design Certification",
                        "freeCodeCamp",
                        "Free Verified Certification",
                        "Free",
                        "https://www.freecodecamp.org/learn/2022/responsive-web-design/",
                        "Includes 300 hours of hands-on CSS Flexbox, Grid, accessibility, and modern responsive layouts."
                ),
                new CertificationItem(
                        "JavaScript Algorithms and Data Structures",
                        "freeCodeCamp",
                        "Free Verified Certification",
                        "Free",
                        "https://www.freecodecamp.org/learn/javascript-algorithms-and-data-structures-v8/",
                        "Solidifies ES6+ JavaScript concepts, DOM manipulation, functional programming, and algorithmic thinking."
                )
        ));
        frontendRole.setRecommendedProjects(List.of(
                new ProjectRecommendation(
                        "Interactive Kanban Project Board",
                        "Create a responsive task management board featuring drag-and-drop workflow, task filtering by priority, and local state persistence.",
                        "Beginner-Friendly",
                        List.of("React.js", "HTML5 Drag & Drop API", "CSS Modules / Tailwind", "LocalStorage"),
                        List.of("Component modularity", "Custom React hooks", "State persistence"),
                        "Architected an intuitive Kanban UI with responsive layouts, fluid drag-and-drop interactions, and zero latency state updates."
                ),
                new ProjectRecommendation(
                        "E-Commerce Product Discovery & Filter Engine",
                        "Develop a product browsing web app with instant client-side search, multi-facet filtering (price, category, rating), and shopping cart state.",
                        "Beginner-Friendly",
                        List.of("JavaScript (ES6+)", "React", "REST API Integration", "CSS Flexbox/Grid"),
                        List.of("Debounced search inputs", "Asynchronous API fetching", "Accessible UI components"),
                        "Built a dynamic product discovery portal handling paginated REST endpoints with debounced real-time search and filter controls."
                )
        ));
        frontendRole.setGeneralTips(List.of(
                "Link live deployed demos (Vercel, Netlify, or GitHub Pages) next to every project.",
                "Highlight accessibility standards (ARIA, semantic HTML) and responsive design breakpoints.",
                "Showcase proficiency in modern CSS (Flexbox, CSS Grid, Tailwind CSS)."
        ));
        roleRegistry.put(frontendRole.getId(), frontendRole);

        // 3. Full Stack Developer
        JobRole fullStackRole = new JobRole(
                "fullstack-developer",
                "Full Stack Developer",
                "Bridges front-end user interfaces and back-end services, handling databases, REST APIs, and deployment pipelines.",
                List.of("Java", "Spring Boot", "JavaScript", "React", "SQL", "REST APIs", "Git", "HTML5/CSS3"),
                List.of("TypeScript", "Docker", "Node.js", "PostgreSQL", "MongoDB", "CI/CD", "AWS")
        );
        fullStackRole.setRecommendedCertifications(List.of(
                new CertificationItem(
                        "Meta Full-Stack Developer Professional Certificate",
                        "Meta on Coursera",
                        "Professional Certificate",
                        "Free Audit / Paid Certificate",
                        "https://www.coursera.org/professional-certificates/meta-full-stack-developer",
                        "Hands-on program covering client-side React, server architectures, databases, and deployment pipelines."
                ),
                new CertificationItem(
                        "Full Stack Open",
                        "University of Helsinki",
                        "Free In-Depth University Course",
                        "Free",
                        "https://fullstackopen.com/",
                        "Rigorous university-level curriculum on single-page apps, REST APIs, databases, containers, and testing."
                ),
                new CertificationItem(
                        "AWS Certified Cloud Practitioner (CLF-C02)",
                        "Amazon Web Services",
                        "Professional Industry Certification",
                        "Paid Exam",
                        "https://aws.amazon.com/certification/certified-cloud-practitioner/",
                        "Foundational credential proving knowledge of cloud deployment, databases, IAM security, and serverless hosting."
                )
        ));
        fullStackRole.setRecommendedProjects(List.of(
                new ProjectRecommendation(
                        "Collaborative Task & Sprint Planning Workspace",
                        "Build an end-to-end management tool connecting a React single-page frontend with a Spring Boot REST API and PostgreSQL database.",
                        "Intermediate",
                        List.of("Spring Boot", "React.js", "PostgreSQL", "JWT Authentication", "Docker"),
                        List.of("JWT token lifecycle", "Full-stack error handling", "Docker-compose environment setup"),
                        "Engineered full-stack SaaS workspace integrating Spring Boot REST APIs with React UI, managing 20+ database tables with JWT auth."
                ),
                new ProjectRecommendation(
                        "Campus Event Discovery & Booking Portal",
                        "Create an online event ticketing and seat reservation application with administrative event creation and email RSVP confirmations.",
                        "Intermediate",
                        List.of("Java / Spring Boot", "HTML/CSS/JS", "MySQL", "REST APIs", "Git"),
                        List.of("Relational database schema design", "Transactional ticket booking logic", "RESTful communication"),
                        "Delivered full-stack ticketing platform supporting concurrent seat reservation transactions and automated email notifications."
                )
        ));
        fullStackRole.setGeneralTips(List.of(
                "Clearly separate frontend and backend responsibilities in your bullet points.",
                "Highlight database design, database normalization, and SQL query optimizations.",
                "Demonstrate knowledge of authentication mechanisms (JWT, session cookies, OAuth2)."
        ));
        roleRegistry.put(fullStackRole.getId(), fullStackRole);

        // 4. Python Developer
        JobRole pythonRole = new JobRole(
                "python-developer",
                "Python Developer",
                "Focuses on backend systems, automation scripts, web frameworks (FastAPI/Django/Flask), and data manipulation.",
                List.of("Python", "OOP", "SQL", "Git", "REST APIs", "Django / FastAPI"),
                List.of("Flask", "PostgreSQL", "Docker", "pytest", "Pandas", "Celery", "Linux")
        );
        pythonRole.setRecommendedCertifications(List.of(
                new CertificationItem(
                        "PCEP - Certified Entry-Level Python Programmer",
                        "Python Institute",
                        "Professional Industry Certification",
                        "Paid Exam",
                        "https://pythoninstitute.org/pcep",
                        "Recognized credential certifying proficiency in Python fundamentals, data structures, and standard libraries."
                ),
                new CertificationItem(
                        "Google IT Automation with Python Professional Certificate",
                        "Google on Coursera",
                        "Professional Certificate",
                        "Free Audit / Paid Certificate",
                        "https://www.coursera.org/professional-certificates/google-it-automation-with-python",
                        "Covers practical Python programming, Git version control, operating system automation, and debugging."
                ),
                new CertificationItem(
                        "Scientific Computing with Python",
                        "freeCodeCamp",
                        "Free Verified Certification",
                        "Free",
                        "https://www.freecodecamp.org/learn/scientific-computing-with-python/",
                        "Hands-on projects solidifying object-oriented Python, algorithms, and practical programming."
                )
        ));
        pythonRole.setRecommendedProjects(List.of(
                new ProjectRecommendation(
                        "Asynchronous REST API for Health & Habit Tracking",
                        "Develop a high-performance backend using FastAPI, Pydantic validation, and SQLite/PostgreSQL with automatic OpenAPI documentation.",
                        "Beginner-Friendly",
                        List.of("Python 3", "FastAPI", "SQLAlchemy", "Pydantic", "pytest"),
                        List.of("Asynchronous Python (async/await)", "ORM modeling", "Automated unit testing"),
                        "Developed asynchronous REST API using FastAPI and SQLAlchemy, achieving sub-50ms query response times with full test coverage."
                ),
                new ProjectRecommendation(
                        "Automated Real Estate / Tech Job Alert Scraper",
                        "Write a reliable scraping and notification service using BeautifulSoup, SQLite, and automated scheduling with email dispatch.",
                        "Beginner-Friendly",
                        List.of("Python", "BeautifulSoup / Playwright", "SQLite", "cron / schedule"),
                        List.of("HTML parsing & error recovery", "Data deduplication", "Automated alert pipelines"),
                        "Built robust Python data pipeline aggregating listings from multiple portals and sending scheduled email digests."
                )
        ));
        pythonRole.setGeneralTips(List.of(
                "Show clean code practices: PEP 8 adherence, virtual environments, and type hinting.",
                "Detail your test suite using pytest or unittest.",
                "Mention async frameworks (FastAPI) or enterprise frameworks (Django) depending on the job description."
        ));
        roleRegistry.put(pythonRole.getId(), pythonRole);

        // 5. Data Analyst
        JobRole dataAnalystRole = new JobRole(
                "data-analyst",
                "Data Analyst",
                "Extracts business insights from structured datasets using SQL, Python, Excel modeling, and visualization dashboards.",
                List.of("SQL", "Python", "Excel / Spreadsheets", "Pandas", "Data Visualization", "Power BI / Tableau"),
                List.of("NumPy", "Matplotlib / Seaborn", "Statistics", "Data Cleaning", "EDA", "Git")
        );
        dataAnalystRole.setRecommendedCertifications(List.of(
                new CertificationItem(
                        "Google Data Analytics Professional Certificate",
                        "Google on Coursera",
                        "Professional Certificate",
                        "Free Audit / Paid Certificate",
                        "https://www.coursera.org/professional-certificates/google-data-analytics",
                        "Industry-standard program covering data cleaning, analysis, SQL queries, R/Python, and Tableau dashboards."
                ),
                new CertificationItem(
                        "Microsoft Certified: Power BI Data Analyst Associate (PL-300)",
                        "Microsoft",
                        "Professional Industry Certification",
                        "Paid Exam",
                        "https://learn.microsoft.com/en-us/credentials/certifications/data-analyst-associate/",
                        "Validates enterprise data modeling, DAX formulas, interactive report creation, and dashboard sharing."
                ),
                new CertificationItem(
                        "Data Analysis with Python Certification",
                        "freeCodeCamp",
                        "Free Verified Certification",
                        "Free",
                        "https://www.freecodecamp.org/learn/data-analysis-with-python/",
                        "Covers NumPy, Pandas, Matplotlib, and practical data exploration tasks on real-world datasets."
                )
        ));
        dataAnalystRole.setRecommendedProjects(List.of(
                new ProjectRecommendation(
                        "Customer Retention & Churn Analysis Dashboard",
                        "Perform exploratory data analysis on a multi-thousand row telecom/SaaS dataset, identifying key churn indicators and publishing an interactive dashboard.",
                        "Beginner-Friendly",
                        List.of("Python", "Pandas", "Matplotlib / Seaborn", "Power BI / Tableau"),
                        List.of("Handling missing data and outliers", "Correlation matrices", "Actionable business storytelling"),
                        "Analyzed 10,000+ customer records in Python and created Power BI dashboards that isolated top factors contributing to 22% user churn."
                ),
                new ProjectRecommendation(
                        "E-Commerce Sales Performance & KPI Analysis",
                        "Write complex SQL queries (window functions, CTEs, aggregations) to calculate monthly recurring revenue, retention cohorts, and product margins.",
                        "Beginner-Friendly",
                        List.of("PostgreSQL / MySQL", "SQL CTEs & Window Functions", "Excel / Google Sheets"),
                        List.of("Advanced SQL querying", "Cohort analysis", "Automated spreadsheet models"),
                        "Authored multi-table SQL queries utilizing CTEs and window functions to uncover revenue trends across 15+ product categories."
                )
        ));
        dataAnalystRole.setGeneralTips(List.of(
                "Emphasize business impact and findings rather than just describing the tools used.",
                "Detail SQL complexity: mention Joins, CTEs, Window Functions, and Group By Aggregations.",
                "Include links to GitHub notebooks (Jupyter) or Tableau Public portfolios."
        ));
        roleRegistry.put(dataAnalystRole.getId(), dataAnalystRole);

        // 6. Software Engineer
        JobRole sweRole = new JobRole(
                "software-engineer",
                "Software Engineer",
                "Emphasizes foundational computer science, data structures, algorithms, modular object-oriented design, and problem solving.",
                List.of("Data Structures & Algorithms", "OOP", "Java / Python / C++", "SQL", "Git", "REST APIs", "Problem Solving"),
                List.of("System Design Fundamentals", "Unit Testing", "Operating Systems", "Computer Networks", "Linux", "Docker")
        );
        sweRole.setRecommendedCertifications(List.of(
                new CertificationItem(
                        "AWS Certified Developer - Associate",
                        "Amazon Web Services",
                        "Professional Industry Certification",
                        "Paid Exam",
                        "https://aws.amazon.com/certification/certified-developer-associate/",
                        "Validates core software engineering deployment, microservice architectures, and cloud databases."
                ),
                new CertificationItem(
                        "Google IT Support / Automation Professional Certificate",
                        "Google on Coursera",
                        "Professional Certificate",
                        "Free Audit / Paid Certificate",
                        "https://www.coursera.org/",
                        "Strengthens operating system internals, computer networking, bash scripting, and system troubleshooting."
                ),
                new CertificationItem(
                        "Algorithms and Data Structures Course",
                        "freeCodeCamp / Coursera",
                        "Free Verified Learning",
                        "Free",
                        "https://www.freecodecamp.org/",
                        "Covers asymptotic analysis (Big-O), trees, graphs, dynamic programming, sorting, and recursion."
                )
        ));
        sweRole.setRecommendedProjects(List.of(
                new ProjectRecommendation(
                        "High-Throughput In-Memory LRU Cache",
                        "Implement a multithreaded Least Recently Used (LRU) cache from scratch using hash maps and doubly-linked lists with thread synchronization.",
                        "Intermediate",
                        List.of("Java / C++", "Concurrency", "Data Structures", "JUnit"),
                        List.of("O(1) lookup and eviction logic", "Thread-safety primitives (ReentrantLock/synchronized)", "Comprehensive unit testing"),
                        "Engineered custom O(1) in-memory LRU cache with thread-safe read/write locking, verified with concurrent stress tests."
                ),
                new ProjectRecommendation(
                        "Distributed URL Shortener Service",
                        "Build a scalable URL shortening engine utilizing Base62 encoding, database indexing, and cache eviction strategies.",
                        "Intermediate",
                        List.of("Java / Python", "REST APIs", "SQL / Redis", "Git"),
                        List.of("Hashing & Base conversion", "Handling collision resolution", "RESTful API design"),
                        "Architected URL shortener service processing 10k+ simulated redirects with Base62 encoding and indexed database retrieval."
                )
        ));
        sweRole.setGeneralTips(List.of(
                "Highlight algorithmic efficiency (Big-O time and space complexity) in project descriptions.",
                "List competitive programming or coding platform experience (LeetCode, HackerRank, Codeforces) if applicable.",
                "Detail software design principles (modular design, abstraction, clean code)."
        ));
        roleRegistry.put(sweRole.getId(), sweRole);
    }

    public List<JobRole> getAllRoles() {
        return new ArrayList<>(roleRegistry.values());
    }

    public JobRole getRoleById(String roleId) {
        if (roleId == null || roleId.isBlank()) {
            return roleRegistry.get("java-developer");
        }
        JobRole role = roleRegistry.get(roleId.toLowerCase().trim());
        if (role != null) {
            return role;
        }

        // Check if title matches loosely
        for (JobRole r : roleRegistry.values()) {
            if (r.getTitle().equalsIgnoreCase(roleId.trim())) {
                return r;
            }
        }

        // Return a custom role synthesis
        return createCustomRole(roleId);
    }

    public JobRole createCustomRole(String customRoleTitle) {
        JobRole customRole = new JobRole();
        String title = (customRoleTitle == null || customRoleTitle.isBlank()) ? "Software Developer" : customRoleTitle.trim();
        customRole.setId("custom-" + UUID.randomUUID().toString().substring(0, 8));
        customRole.setTitle(title);
        customRole.setDescription("Custom job role configured by the user: " + title);
        
        // Base engineering skills appropriate for any tech role
        customRole.setRequiredSkills(List.of("Problem Solving", "Git", "Data Structures", "REST APIs", "SQL", "OOP"));
        customRole.setSecondarySkills(List.of("Unit Testing", "System Design", "Documentation", "CI/CD"));

        customRole.setRecommendedCertifications(List.of(
                new CertificationItem(
                        "Foundational Cloud / Engineering Certification",
                        "AWS / Microsoft / Google Cloud",
                        "Professional Industry Certification",
                        "Paid Exam",
                        "https://aws.amazon.com/certification/",
                        "Standard industry credential demonstrating cloud and software fundamentals."
                ),
                new CertificationItem(
                        "Developer Certification Program",
                        "freeCodeCamp",
                        "Free Verified Certification",
                        "Free",
                        "https://www.freecodecamp.org/",
                        "Comprehensive practical programming curriculum covering algorithms, web systems, and data structures."
                )
        ));

        customRole.setRecommendedProjects(List.of(
                new ProjectRecommendation(
                        "Role-Specific Capstone Application",
                        "Build an end-to-end portfolio application demonstrating the core technologies demanded in your target job description.",
                        "Intermediate",
                        List.of("Git", "REST APIs", "Relational Database", "Automated Testing"),
                        List.of("End-to-end system design", "Clean architecture", "Comprehensive README documentation"),
                        "Designed and deployed production-ready application demonstrating modern engineering best practices and test coverage."
                )
        ));

        customRole.setGeneralTips(List.of(
                "Tailor resume keywords directly to the specific job posting requirements.",
                "Include quantifiable outcomes (e.g. reduced load time by 30%, handled 500+ daily requests).",
                "Keep resume formatting clear, single-column, and easy for ATS scanners to parse."
        ));

        return customRole;
    }
}
