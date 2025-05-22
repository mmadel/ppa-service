package com.cob.ppa.controller;

import com.cob.ppa.constant.ClinicImportStatus;
import com.cob.ppa.service.batch.FindClinicEligibilityRecordsService;
import com.cob.ppa.service.batch.FindPatientRecordImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
