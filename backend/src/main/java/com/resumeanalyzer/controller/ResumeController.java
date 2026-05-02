package com.resumeanalyzer.controller;

import com.resumeanalyzer.dto.AnalysisResponse;
import com.resumeanalyzer.dto.AnalyzeRequest;
import com.resumeanalyzer.dto.HistoryResponse;
import com.resumeanalyzer.dto.UploadResponse;
import com.resumeanalyzer.service.ResumeService;
import com.resumeanalyzer.service.SkillMatchingService;
import com.resumeanalyzer.service.TextExtractionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final TextExtractionService textExtraction;
    private final SkillMatchingService skillMatching;

    @PostMapping("/upload-resume")
    public ResponseEntity<?> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            String text = textExtraction.extractText(file);
            String preview = text.length() > 300 ? text.substring(0, 300) + "…" : text;

            return ResponseEntity.ok(UploadResponse.builder()
                    .filename(file.getOriginalFilename())
                    .message("Resume uploaded and text extracted successfully.")
                    .extractedTextPreview(preview)
                    .extractedText(text)
                    .build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Could not read file: " + e.getMessage()));
        }
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalysisResponse> analyze(@Valid @RequestBody AnalyzeRequest req) {
        return ResponseEntity.ok(resumeService.analyze(req));
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<AnalysisResponse> getResult(@PathVariable Long id) {
        return ResponseEntity.ok(resumeService.getById(id));
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoryResponse>> getHistory() {
        return ResponseEntity.ok(resumeService.getHistory());
    }

    @GetMapping("/job-roles")
    public ResponseEntity<List<String>> getJobRoles() {
        return ResponseEntity.ok(skillMatching.getAvailableJobRoles());
    }
}
