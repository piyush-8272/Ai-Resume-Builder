package com.resumeanalyzer.service;

import com.resumeanalyzer.dto.AnalysisResponse;
import com.resumeanalyzer.dto.AnalyzeRequest;
import com.resumeanalyzer.dto.HistoryResponse;
import com.resumeanalyzer.model.ResumeAnalysis;
import com.resumeanalyzer.repository.ResumeAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeAnalysisRepository repository;
    private final SkillMatchingService skillMatching;

    @Transactional
    public AnalysisResponse analyze(AnalyzeRequest req) {
        List<String> found   = skillMatching.extractSkillsFound(req.getResumeText(), req.getJobRole());
        List<String> missing = skillMatching.extractMissingSkills(req.getResumeText(), req.getJobRole());
        double score         = skillMatching.calculateScore(found.size(), req.getJobRole());
        List<String> suggestions = skillMatching.generateSuggestions(missing, req.getJobRole(), score);

        ResumeAnalysis entity = ResumeAnalysis.builder()
                .filename(req.getFilename() != null ? req.getFilename() : "resume")
                .jobRole(req.getJobRole())
                .extractedText(req.getResumeText())
                .skillsFound(found)
                .missingSkills(missing)
                .suggestions(suggestions)
                .score(score)
                .build();

        return toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public AnalysisResponse getById(Long id) {
        ResumeAnalysis entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found: " + id));
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<HistoryResponse> getHistory() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toHistoryResponse)
                .collect(Collectors.toList());
    }

    private AnalysisResponse toResponse(ResumeAnalysis e) {
        return AnalysisResponse.builder()
                .id(e.getId())
                .filename(e.getFilename())
                .jobRole(e.getJobRole())
                .skillsFound(e.getSkillsFound())
                .missingSkills(e.getMissingSkills())
                .suggestions(e.getSuggestions())
                .score(e.getScore())
                .createdAt(e.getCreatedAt())
                .build();
    }

    private HistoryResponse toHistoryResponse(ResumeAnalysis e) {
        return HistoryResponse.builder()
                .id(e.getId())
                .filename(e.getFilename())
                .jobRole(e.getJobRole())
                .score(e.getScore())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
