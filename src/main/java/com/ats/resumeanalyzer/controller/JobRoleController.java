package com.ats.resumeanalyzer.controller;

import com.ats.resumeanalyzer.model.JobRole;
import com.ats.resumeanalyzer.service.JobRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for retrieving available target job roles and their skill requirements.
 */
@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class JobRoleController {

    private final JobRoleService jobRoleService;

    public JobRoleController(JobRoleService jobRoleService) {
        this.jobRoleService = jobRoleService;
    }

    /**
     * Retrieves all predefined target job roles.
     */
    @GetMapping
    public ResponseEntity<List<JobRole>> getAllRoles() {
        return ResponseEntity.ok(jobRoleService.getAllRoles());
    }

    /**
     * Retrieves a single role by identifier.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobRole> getRoleById(@PathVariable String id) {
        return ResponseEntity.ok(jobRoleService.getRoleById(id));
    }
}
