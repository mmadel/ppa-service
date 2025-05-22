package com.cob.ppa.controller;

import com.cob.ppa.response.patient.record.model.PatientMedicalRecordUploadResponse;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/file/processing")
public class FileProcessingController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importBenefitsJob;

    @PostMapping("/upload")
    public ResponseEntity test(@RequestParam("benefitFile") MultipartFile file
            , @RequestParam("documentFile") MultipartFile documentFile
            , @RequestParam("paymentFile") MultipartFile paymentFile) throws JobExecutionException, IOException {
        Path benefitTmpFile = Files.createTempFile("benefits_", ".csv");
        file.transferTo(benefitTmpFile);

        Path documentTmpFile = Files.createTempFile("document_", ".csv");
        documentFile.transferTo(documentTmpFile);

        Path paymentTmpFile = Files.createTempFile("payment_", ".csv");
        paymentFile.transferTo(paymentTmpFile);
        String pmrbId = UUID.randomUUID().toString();
        JobParameters params = new JobParametersBuilder()
                .addString("benefitFilePath", benefitTmpFile.toString())
                .addString("documentUnitsFilePath", documentTmpFile.toString())
                .addString("paymentFilePath", paymentTmpFile.toString())
                .addString("pmrb-id", pmrbId)
                .toJobParameters();


        jobLauncher.run(importBenefitsJob, params);

        return ResponseEntity.ok().body(PatientMedicalRecordUploadResponse.builder()
                .pmrbId(pmrbId)
                .build());
    }
}
