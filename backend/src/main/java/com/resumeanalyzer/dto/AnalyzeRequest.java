package com.resumeanalyzer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnalyzeRequest {

    @NotBlank(message = "Resume text is required")
    private String resumeText;

    @NotBlank(message = "Job role is required")
    private String jobRole;

    private String filename;
}
