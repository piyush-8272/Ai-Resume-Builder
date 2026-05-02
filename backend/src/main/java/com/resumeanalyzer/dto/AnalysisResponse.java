package com.resumeanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponse {
    private Long id;
    private String filename;
    private String jobRole;
    private List<String> skillsFound;
    private List<String> missingSkills;
    private List<String> suggestions;
    private Double score;
    private LocalDateTime createdAt;
}
