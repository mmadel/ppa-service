package com.cob.ppa.controller;

import com.cob.ppa.constant.BatchStatus;
import com.cob.ppa.constant.ClinicImportStatus;
import com.cob.ppa.service.batch.FindClinicEligibilityRecordsService;
import com.cob.ppa.service.batch.FindPatientRecordImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/batches")
public class BatchesController {

    @Autowired
    FindPatientRecordImportService findPatientRecordImportService;

    @Autowired
    FindClinicEligibilityRecordsService findClinicEligibilityRecordsService;

    @GetMapping("/get")
    public ResponseEntity getPatientMedicalRecordsBatch(@RequestParam(name = "offset", required = false) String offset,
                                                        @RequestParam(name = "limit", required = false) String limit) {
        Pageable paging = PageRequest.of(Integer.parseInt(offset), Integer.parseInt(limit), Sort.by("createdAt").descending());
        return ResponseEntity.ok()
                .body(findPatientRecordImportService.find(paging));
    }

    @GetMapping("/pmr/search")
    public ResponseEntity searchPatientMedicalRecordsBatch(@RequestParam(name = "offset", required = false) String offset,
                                                           @RequestParam(name = "limit", required = false) String limit,
                                                           @RequestParam(name = "status", required = false) BatchStatus status,
                                                           @RequestParam(name = "pmrbId", required = false) String pmrbId,
                                                           @RequestParam(name = "created-at" , required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt) {
        Pageable paging = PageRequest.of(Integer.parseInt(offset), Integer.parseInt(limit), Sort.by("createdAt").descending());
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (createdAt != null) {
            start = createdAt.atStartOfDay();
            end = createdAt.atTime(LocalTime.MAX);
        }
        return ResponseEntity.ok()
                .body(findPatientRecordImportService.search(paging, status, pmrbId, start, end));
    }

    @GetMapping("/cer/get/pmrbId/{pmrbId}")
    public ResponseEntity getClinicEligibilityRecords(@PathVariable String pmrbId) {
        return ResponseEntity.ok()
                .body(findClinicEligibilityRecordsService.find(pmrbId));
    }

    @GetMapping("/cer/search")
    public ResponseEntity search(@RequestParam(name = "location", required = false) String location,
                                 @RequestParam(name = "status", required = false) ClinicImportStatus status,
                                 @RequestParam(name = "pmrbId", required = false) String pmrbId,
                                 @RequestParam(name = "lastUpdate", required = false) String lastUpdate) {
        return ResponseEntity.ok()
                .body(findClinicEligibilityRecordsService.search(location, status, pmrbId, lastUpdate != null ? LocalDate.parse(lastUpdate) : null));
    }
}
