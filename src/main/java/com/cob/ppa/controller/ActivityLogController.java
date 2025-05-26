package com.cob.ppa.controller;

import com.cob.ppa.constant.RequestStatus;
import com.cob.ppa.service.activity.log.FindActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/activity-log")
public class ActivityLogController {
    @Autowired
    FindActivityLogService findActivityLogService;
    @GetMapping("/search")
    public ResponseEntity searchPatientMedicalRecordsBatch(@RequestParam(name = "offset", required = false) String offset,
                                                           @RequestParam(name = "limit", required = false) String limit,
                                                           @RequestParam(name = "status", required = false) RequestStatus status,
                                                           @RequestParam(name = "requestId", required = false) String requestId,
                                                           @RequestParam(name = "userName", required = false) String userName,
                                                           @RequestParam(name = "created-at" , required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAt) {
        Pageable paging = PageRequest.of(Integer.parseInt(offset), Integer.parseInt(limit), Sort.by("createdAt").descending());
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (createdAt != null) {
            start = createdAt.atStartOfDay();
            end = createdAt.atTime(LocalTime.MAX);
        }
        return ResponseEntity.ok()
                .body(findActivityLogService.search(paging, status, requestId,userName, start, end));
    }
}
