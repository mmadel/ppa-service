package com.cob.ppa.controller;

import com.cob.ppa.response.patient.record.model.PatientMedicalRecordUploadResponse;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/file/processing")
public class FileProcessingController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importBenefitsJob;

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestParam("benefitFile") MultipartFile file
            , @RequestParam("documentFile") MultipartFile documentFile
            , @RequestParam("paymentFile") MultipartFile paymentFile, HttpServletRequest request) throws JobExecutionException, IOException {
        Path benefitTmpFile = Files.createTempFile("benefits_", ".csv");
        file.transferTo(benefitTmpFile);

        Path documentTmpFile = Files.createTempFile("document_", ".csv");
        documentFile.transferTo(documentTmpFile);

        Path paymentTmpFile = Files.createTempFile("payment_", ".csv");
        paymentFile.transferTo(paymentTmpFile);
        String pmrbId = (String) request.getAttribute("pmrbId");
        JobParameters params = new JobParametersBuilder()
                .addString("benefitFilePath", benefitTmpFile.toString())
                .addString("documentUnitsFilePath", documentTmpFile.toString())
                .addString("paymentFilePath", paymentTmpFile.toString())
                .addString("pmrb-id", pmrbId)
                .toJobParameters();


        JobExecution jobExecution = jobLauncher.run(importBenefitsJob, params);

        if (jobExecution.getExitStatus().getExitCode().equals("FAILED")) {

            String errorMsg = jobExecution.getExecutionContext().getString("errorMessage");
            return ResponseEntity.ok().body(PatientMedicalRecordUploadResponse.builder()
                    .pmrbId(pmrbId)
                    .hasError(true)
                    .errorMessage(errorMsg)
                    .build());
        } else {
            return ResponseEntity.ok().body(PatientMedicalRecordUploadResponse.builder()
                    .pmrbId(pmrbId)
                    .hasError(false)
                    .build());
        }
    }
}
