package com.cob.ppa.controller;

import com.cob.ppa.service.clinic.CacheClinicRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clinic")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ClinicController {
    @Autowired
    CacheClinicRecords cacheClinicRecords;
    @GetMapping("/find")
    @PreAuthorize("hasAnyAuthority('USER_CREATE')")
    public ResponseEntity findAll(){
        return ResponseEntity.ok()
                .body(cacheClinicRecords.cache());
    }
}
