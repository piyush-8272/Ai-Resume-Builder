package com.resumeanalyzer.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class TextExtractionService {

    public String extractText(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("File name is missing.");
        }
        String lower = name.toLowerCase();

        if (lower.endsWith(".pdf")) {
            return extractPdf(file.getInputStream());
        } else if (lower.endsWith(".docx")) {
            return extractDocx(file.getInputStream());
        } else if (lower.endsWith(".doc")) {
            return extractDoc(file.getInputStream());
        }
        throw new IllegalArgumentException("Unsupported format. Upload PDF, DOC, or DOCX.");
    }

    private String extractPdf(InputStream in) throws IOException {
        byte[] bytes = in.readAllBytes();
        try (PDDocument doc = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        }
    }

    private String extractDocx(InputStream in) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(in);
             XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
            return extractor.getText();
        }
    }

    private String extractDoc(InputStream in) throws IOException {
        try (HWPFDocument doc = new HWPFDocument(in);
             WordExtractor extractor = new WordExtractor(doc)) {
            return extractor.getText();
        }
    }
}
