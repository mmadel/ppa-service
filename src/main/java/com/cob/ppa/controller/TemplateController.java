package com.cob.ppa.controller;

import com.cob.ppa.entity.ExcelTemplate;
import com.cob.ppa.repository.ExcelTemplateRepository;
import com.cob.ppa.service.template.UploadExcelTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/templates")
public class TemplateController {
    @Autowired
    UploadExcelTemplate uploadExcelTemplate;
    @Autowired
    private ExcelTemplateRepository templateRepository;
    @PostMapping("/upload")
    public ResponseEntity<?> uploadTemplate(
            @RequestParam("file") MultipartFile file,
            @RequestParam("templateName") String templateName,
            @RequestParam(value = "description", required = false) String description) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a file to upload");
        }

        if (!isExcelFile(file)) {
            return ResponseEntity.badRequest().body("Only Excel files are allowed");
        }

        try {
            ExcelTemplate savedTemplate = uploadExcelTemplate.save(
                    file, templateName, description);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "templateId", savedTemplate.getId(),
                    "templateName", savedTemplate.getTemplateName(),
                    "fileSize", savedTemplate.getFileSize(),
                    "message", "Template uploaded successfully"
            ));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to upload template: " + e.getMessage());
        }
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadTemplate(@PathVariable Long id) {
        Optional<ExcelTemplate> templateOptional = templateRepository.findById(id);

        if (templateOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ExcelTemplate template = templateOptional.get();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + template.getTemplateName() + ".xlsx\"")
                .contentType(MediaType.parseMediaType(template.getFileType()))
                .body(template.getFileContent());
    }
    private boolean isExcelFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null &&
                (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                        contentType.equals("application/vnd.ms-excel"));
    }
}
