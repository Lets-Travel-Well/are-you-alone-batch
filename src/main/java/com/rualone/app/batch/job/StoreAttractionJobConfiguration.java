package com.rualone.app.batch.job;

import com.rualone.app.batch.job.chunk.AttractionApiReader;
import com.rualone.app.batch.job.chunk.AttractionApiWriter;
import com.rualone.app.stationapi.domain.AttractionDto;
import com.rualone.app.stationapi.service.AttractionAPIService;
import com.rualone.app.stationapi.service.AttractionStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class StoreAttractionJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AttractionAPIService attractionAPIService;
    private final AttractionStoreService attractionStoreService;

    @Bean
    public Job storeAttractionJob() {
        return jobBuilderFactory.get("storeAttractionJob")
                .start(storeAttractionStep())
                .build();
    }

    @Bean
    @JobScope
    public Step storeAttractionStep() {
        return stepBuilderFactory.get("storeAttractionStep")
                .<List<AttractionDto>,List<AttractionDto>>chunk(1)
                .reader(attractionApiReader())
                .writer(attractionApiWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<List<AttractionDto>> attractionApiReader() {
        return new AttractionApiReader(attractionAPIService);
    }

    @Bean
    @StepScope
    public ItemWriter<List<AttractionDto>> attractionApiWriter() {
        return new AttractionApiWriter(attractionStoreService);
    }
}