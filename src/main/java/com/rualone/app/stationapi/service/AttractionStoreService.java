package com.rualone.app.stationapi.service;

import com.rualone.app.main.dao.AttractionInfoRepository;
import com.rualone.app.main.dao.GugunRepository;
import com.rualone.app.main.dao.SidoRepository;
import com.rualone.app.main.entity.AttractionInfo;
import com.rualone.app.main.entity.Gugun;
import com.rualone.app.main.entity.Sido;
import com.rualone.app.stationapi.domain.ApiFetchResult;
import com.rualone.app.stationapi.domain.AttractionDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttractionStoreService {

	private static final Logger logger = LoggerFactory.getLogger("file");

	private final AttractionInfoRepository attractionInfoRepository;
	private final SidoRepository sidoRepository;
	private final GugunRepository gugunRepository;

	private ThreadLocal<ApiFetchResult> apiFetchResultStore = new ThreadLocal<>();

	@Transactional
	@CacheEvict(value = "attraction", allEntries = true)
	public void storeAttractions(List<AttractionDto> attractionList) {
		initApiFetchResult();
		for (AttractionDto attraction : attractionList) {
			storeAttraction(attraction);
		}
		recordFetchResult();
		releaseResult();
	}

	private void initApiFetchResult() {
		apiFetchResultStore.set(new ApiFetchResult());
	}

	private void releaseResult() {
		apiFetchResultStore.remove();
	}

	private void recordFetchResult() {
		ApiFetchResult apiFetchResult = apiFetchResultStore.get();
		logger.info(apiFetchResult.toString());
	}

	private void storeAttraction(AttractionDto attractionDto) {
		Sido sido = sidoRepository.findBySidoCode(attractionDto.getAreacode());
		Gugun gugun = gugunRepository.findByGugunCodeAndSido(attractionDto.getSigungucode(), sido).get();
		AttractionInfo requestAttraction = attractionDto.toEntity(sido, gugun);

		Optional<AttractionInfo> findAttraction = attractionInfoRepository.findByContentId(attractionDto.getContentid());

		if (findAttraction.isEmpty()) {
			attractionInfoRepository.save(requestAttraction);
			increaseCountOfCreated();
			return;
		}

		AttractionInfo storedAttraction = findAttraction.get();
		boolean isChanged = storedAttraction.update(requestAttraction);
		if (isChanged) {
			increaseCountOfChange();
			return;
		}
		increaseCountOfNotChanged();
	}

	private void increaseCountOfChange() {
		ApiFetchResult storedResult = apiFetchResultStore.get();
		storedResult.increaseCountOfChanged();
	}

	private void increaseCountOfNotChanged() {
		ApiFetchResult storedResult = apiFetchResultStore.get();
		storedResult.increaseCountOfNotChanged();
	}

	private void increaseCountOfCreated() {
		ApiFetchResult storedResult = apiFetchResultStore.get();
		storedResult.increaseCountOfCreated();
	}
}