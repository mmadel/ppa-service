package com.cob.ppa.service.batch.configuration;

import com.cob.ppa.dto.PaymentDto;
import com.cob.ppa.service.batch.JobDataHolder;
import com.cob.ppa.service.batch.reader.PatientPaymentReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PatientPaymentConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Bean
    public Step processPaymentStep(ItemReader<PaymentDto> paymentReader, ItemWriter<PaymentDto> paymentWriter) {
        return stepBuilderFactory.get("processPaymentStep")
                .<PaymentDto, PaymentDto>chunk(1000)
                .reader(paymentReader)
                .writer(paymentWriter)
                .build();
    }
    @Bean
    @StepScope
    public ItemReader<PaymentDto> paymentReader(
            @Value("#{jobParameters['paymentFilePath']}") String tempPath) throws Exception {
        return new PatientPaymentReader(tempPath);
    }
    @Bean
    @StepScope
    public ItemWriter<PaymentDto> paymentWriter(JobDataHolder holder) {
        return items -> {
            List<PaymentDto> documents = holder.getData("payments");
            synchronized(documents) {
                documents.addAll(items);
            }
        };
    }
}
