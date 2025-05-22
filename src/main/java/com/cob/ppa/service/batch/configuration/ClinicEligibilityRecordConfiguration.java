package com.cob.ppa.service.batch.configuration;

import com.cob.ppa.entity.ExcelTemplate;
import com.cob.ppa.service.GenerateClinicEligibilityRecord;
import com.cob.ppa.service.batch.JobDataHolder;
import com.cob.ppa.service.patient.record.BuildPatientMedicalRecord;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClinicEligibilityRecordConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    BuildPatientMedicalRecord buildPatientMedicalRecord;
    @Autowired
    GenerateClinicEligibilityRecord clinicEligibilityRecord;
    @Bean
    public Step clinicEligibilityRecordStep(Tasklet clinicEligibilityRecordTasklet) {
        return stepBuilderFactory.get("clinicEligibilityRecordStep")
                .tasklet(clinicEligibilityRecordTasklet)
                .allowStartIfComplete(true) // Optional: allows reruns if needed
                .build();
    }

    @Bean
    @StepScope
    public Tasklet clinicEligibilityRecordTasklet(@Value("#{jobParameters['pmrb-id']}") String pmrbId,
                                                  JobDataHolder holder) {
        return (contribution, chunkContext) -> {
            holder.getPatientMedicalRecord();
            clinicEligibilityRecord.generate(holder.getPatientMedicalRecord(),pmrbId);
            holder.clearPatientMedicalRecord();
            return RepeatStatus.FINISHED;
        };
    }

}
