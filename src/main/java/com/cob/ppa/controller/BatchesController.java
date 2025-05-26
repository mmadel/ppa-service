package com.cob.ppa.controller;

import com.cob.ppa.constant.ClinicImportStatus;
import com.cob.ppa.service.batch.FindClinicEligibilityRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cer")
public class BatchesController {


    @Autowired
    FindClinicEligibilityRecordsService findClinicEligibilityRecordsService;

    @GetMapping("/get/pmrbId/{pmrbId}")
    public ResponseEntity getClinicEligibilityRecords(@PathVariable String pmrbId) {
        return ResponseEntity.ok()
                .body(findClinicEligibilityRecordsService.find(pmrbId));
    }

    @GetMapping("/search")
    public ResponseEntity search(@RequestParam(name = "location", required = false) String location,
                                 @RequestParam(name = "status", required = false) ClinicImportStatus status,
                                 @RequestParam(name = "pmrbId", required = false) String pmrbId,
                                 @RequestParam(name = "lastUpdate", required = false) String lastUpdate) {
        return ResponseEntity.ok()
                .body(findClinicEligibilityRecordsService.search(location, status, pmrbId, lastUpdate != null ? LocalDate.parse(lastUpdate) : null));
    }
}
