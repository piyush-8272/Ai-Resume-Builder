package com.resumeanalyzer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resume_analyses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(name = "job_role", nullable = false)
    private String jobRole;

    @Column(name = "extracted_text", columnDefinition = "TEXT")
    private String extractedText;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "analysis_skills_found",
            joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "skill")
    @Builder.Default
    private List<String> skillsFound = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "analysis_missing_skills",
            joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "skill")
    @Builder.Default
    private List<String> missingSkills = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "analysis_suggestions",
            joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "suggestion", columnDefinition = "TEXT")
    @Builder.Default
    private List<String> suggestions = new ArrayList<>();

    @Column(nullable = false)
    private Double score;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
