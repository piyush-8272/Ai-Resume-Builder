package com.resumeanalyzer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    private Long id;
    private String filename;
    private String jobRole;
    private Double score;
    private LocalDateTime createdAt;
}
