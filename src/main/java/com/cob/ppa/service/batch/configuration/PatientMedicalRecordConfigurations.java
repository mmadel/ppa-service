package com.cob.ppa.service.batch.configuration;

import com.cob.ppa.dto.PatientMedicalRecordDTO;
import com.cob.ppa.service.batch.JobDataHolder;
import com.cob.ppa.service.patient.record.BuildPatientMedicalRecord;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class PatientMedicalRecordConfigurations {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    BuildPatientMedicalRecord buildPatientMedicalRecord;
    @Bean
    public Step patientRecordStep(
            ItemReader<PatientMedicalRecordDTO> patientRecordReader,
            ItemProcessor<PatientMedicalRecordDTO, PatientMedicalRecordDTO> patientRecordProcessor,
            ItemWriter<PatientMedicalRecordDTO> jdbcWriter
    ) {
        return stepBuilderFactory.get("patientRecordStep")
                .<PatientMedicalRecordDTO, PatientMedicalRecordDTO>chunk(1000)
                .reader(patientRecordReader)
                .processor(patientRecordProcessor)
                .writer(jdbcWriter)
                .build();
    }
    @Bean
    @StepScope
    public ListItemReader<PatientMedicalRecordDTO> patientRecordReader(JobDataHolder holder) {
        List<PatientMedicalRecordDTO> records = buildPatientMedicalRecord.create(
                holder.getData("benefits"),
                holder.getData("documents"),
                holder.getData("payments")
        );
        holder.setPatientMedicalRecord(records);
        holder.clearStorage();
        return new ListItemReader<>(records);
    }
    @Bean
    @StepScope
    public ItemProcessor<PatientMedicalRecordDTO, PatientMedicalRecordDTO> patientRecordProcessor() {
        return item -> {
            // Optional: validation or transformation
            return item;
        };
    }
    @Bean
    @StepScope
    public JdbcBatchItemWriter<PatientMedicalRecordDTO> jdbcWriter(DataSource dataSource) {
        JdbcBatchItemWriter<PatientMedicalRecordDTO> writer = new JdbcBatchItemWriter<>();
        try {
            writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
            writer.setSql(
                    "INSERT INTO patient_medical_record (" +
                            "appointment_date, emr_id, charge_amount, client_payment, clinic_name, insurance_id, " +
                            "insurance_name, patient_name, secondary_insurance_id, secondary_insurance_name, " +
                            "visit_id, visit_status" +
                            ") VALUES (" +
                            ":dos, :emrId, :chargeAmount, :clientPayment, :clinicName, :insuranceId, " +
                            ":insuranceName, :patientName, :secondaryInsuranceId, :secondaryInsuranceName, " +
                            ":visitId, :visitStatus" +
                            ") ON DUPLICATE KEY UPDATE " +
                            "visit_id = VALUES(visit_id), " +
                            "charge_amount = VALUES(charge_amount), " +
                            "client_payment = VALUES(client_payment)"
            );
            writer.setDataSource(dataSource);
        } catch (Exception ex) {


        }
        return writer;
    }
}
