package com.rualone.app.batch.job.chunk;

import com.rualone.app.stationapi.domain.AttractionDto;
import com.rualone.app.stationapi.service.AttractionStoreService;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class AttractionApiWriter implements ItemWriter<List<AttractionDto>> {

    private final AttractionStoreService attractionStoreService;

    public AttractionApiWriter(AttractionStoreService attractionStoreService) {
        this.attractionStoreService = attractionStoreService;
    }

    @Override
    public void write(List<? extends List<AttractionDto>> items) {
        items.forEach(attractionStoreService::storeAttractions);
    }
}