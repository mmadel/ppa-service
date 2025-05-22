package com.cob.ppa.controller;

import com.cob.ppa.service.ExportClinicEligibilityReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/export")
public class ExportClinicEligibilityReportsController {

    @Autowired
    ExportClinicEligibilityReportService exportClinicEligibilityReportService;

    @PostMapping("/get/eligibility")
    public ResponseEntity test(@RequestBody List<Long> cerIds) throws IOException {
        byte[] zipBytes = exportClinicEligibilityReportService.export(cerIds);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"Eligibility_Clinic_Sheets.zip\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipBytes);
    }
}
