package com.cob.ppa.service.batch.configuration;

import com.cob.ppa.dto.DocumentDto;
import com.cob.ppa.service.batch.reader.DocumentUnitReader;
import com.cob.ppa.service.batch.JobDataHolder;
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
public class DocumentUnitsConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Bean
    public Step processDocumentUnitsStep(ItemReader<DocumentDto> documentUnitsReader,
                                         ItemWriter<DocumentDto> documentWriter) {
        return stepBuilderFactory.get("processDocumentUnitsStep")
                .<DocumentDto, DocumentDto>chunk(1000)
                .reader(documentUnitsReader)
                .writer(documentWriter)
                .build();
    }
    @Bean
    @StepScope
    public ItemReader<DocumentDto> documentUnitsReader(
            @Value("#{jobParameters['documentUnitsFilePath']}") String tempPath) throws Exception {
        return new DocumentUnitReader(tempPath);
    }
    @Bean
    @StepScope
    public ItemWriter<DocumentDto> documentWriter(JobDataHolder holder) {
        return items -> {
            List<DocumentDto> documents = holder.getData("documents");
            synchronized(documents) {
                documents.addAll(items);
            }
        };
    }
}
