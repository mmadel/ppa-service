package com.cob.ppa.service.batch.configuration;

import com.cob.ppa.dto.BenefitDto;
import com.cob.ppa.service.batch.reader.BenefitReader;
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
public class BenefitJobConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public ItemReader<BenefitDto> benefitReader(
            @Value("#{jobParameters['benefitFilePath']}") String tempPath) throws Exception {
        return new BenefitReader(tempPath);
    }

    @Bean
    @StepScope
    public ItemWriter<BenefitDto> benefitDataWriter(JobDataHolder holder) {
        return items -> {
            List<BenefitDto> benefits = holder.getData("benefits");
            synchronized (benefits) {
                benefits.addAll(items);
            }
        };
    }

    @Bean
    public Step processBenefitsStep(ItemReader<BenefitDto> benefitReader,
                                    ItemWriter<BenefitDto> benefitDataWriter) {
        return stepBuilderFactory.get("processBenefitsStep")
                .<BenefitDto, BenefitDto>chunk(1000)
                .reader(benefitReader)
                .writer(benefitDataWriter)
                .build();
    }
}
