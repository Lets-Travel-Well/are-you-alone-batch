package com.rualone.app.stationapi.service;

import com.rualone.app.stationapi.domain.AttractionDto;
import com.rualone.app.stationapi.domain.ResponseFieldName;
import com.rualone.app.stationapi.exception.ApiResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@Service
@Transactional(readOnly = true)
public class AttractionAPIService {

	private static final Logger logger = LoggerFactory.getLogger("file");
	public static final int INIT_PAGE = 1;
	public static final int INIT_PER_PAGE = 100;
	public static final String OS = "win";
	public static final String appName = "rualone";

	private final RestTemplate restTemplate;

	@Value("${data-portal.url}")
	private String baseRequestUrl;

	@Value("${data-portal.service-key}")
	private String serviceKey;

	@Autowired
	public AttractionAPIService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = initRestTemplate(restTemplateBuilder);
//		restTemplate.getInterceptors().add((request, body, execution) -> {
//			ClientHttpResponse response = execution.execute(request,body);
//			response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
//			System.out.println(response.getHeaders());
//			System.out.println(response.getStatusCode());
//			System.out.println(Arrays.toString(response.getBody().readNBytes(100)));
//			return response;
//		});
	}

	private RestTemplate initRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		HttpComponentsClientHttpRequestFactory factory = makeRequestFactory(makeHttpClient());
		return restTemplateBuilder.requestFactory(() -> factory).build();
	}

	private HttpComponentsClientHttpRequestFactory makeRequestFactory(HttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		factory.setConnectTimeout(10 * 1000);
		factory.setReadTimeout(10 * 1000);
		return factory;
	}

	private HttpClient makeHttpClient() {
		return HttpClientBuilder.create()
				.setMaxConnTotal(100)
				.setMaxConnPerRoute(1)		//멀티 스레드로 돌리면 스레드 갯수만큼 할당하면 될 것 같다.
				.build();
	}

	public List<AttractionDto> fetchAttraction(int page) {
		return fetchAttraction(page, INIT_PER_PAGE);
	}

	public List<AttractionDto> fetchAttraction(int page, int pageSize) {
		Map result = requestAPICall(page, pageSize);
		return convertDataToAttractionEntity(result);
	}

	protected int getTotalCount(int pageSize) {
		Map result = requestAPICall(INIT_PAGE, pageSize);

		try {
			sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return (Integer)result.get("totalCount");
	}

	protected Map requestAPICall(int page, int pageSize) {
		String requestUrl = getApiPath(page, pageSize);
		System.out.println(requestUrl);
		return callRestApi(requestUrl);
	}

	protected Map callRestApi(String requestUrl) {
		URI wrappedURI = null;
		try {
			wrappedURI = new URI(requestUrl);
			Map response = (Map) restTemplate.exchange(wrappedURI, HttpMethod.GET, getHeaders(), Map.class).getBody().get("response");
			return (Map) response.get("body");
		} catch (HttpClientErrorException ex) {
			/* RestTemplate은 DefaultResponseErrorHandler를 사용하여 에러를 처리함.
				DefaultResponseErrorHandler는 4xx, 5xx 에러에 대해 HttpClientErrorException 예외를 발생시킴
				이 오류는 어떻게 할 수 없으니 로깅 후 다시 오류를 던지자.
			 */
			logger.error("요청이 정상적으로 처리되지 못했습니다.\n상태코드 : {} , 응답메세지 : {}", ex.getStatusText(), ex.getMessage());
			throw new ApiResponseException(ex);
		} catch (ResourceAccessException ex) {
			// 타임아웃 오류인 경우 5초 후 재시도한다.
			try {
				sleep(5000); // 5초 후 재시도
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 한번더 오류나면 그냥 오류 처리
			return restTemplate.exchange(wrappedURI, HttpMethod.GET, getHeaders(), Map.class).getBody();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private HttpEntity<?> getHeaders() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.AUTHORIZATION, serviceKey);
		return new HttpEntity<>(httpHeaders);
	}

	private String getApiPath(int page, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseRequestUrl)
			.append("?pageNo=").append(page)
			.append("&numOfRows=").append(pageSize)
				.append("&MobileOS=").append(OS)
				.append("&MobileApp=").append(appName)
			.append("&serviceKey=").append(serviceKey)
				.append("&_type=json");
		return sb.toString();
	}

	private List<AttractionDto> convertDataToAttractionEntity(Map responseBody) {
		LinkedHashMap items = (LinkedHashMap)responseBody.get("items");
		ArrayList<Map> extractedData = (ArrayList<Map>) items.get("item");

		return extractedData.stream().map(data ->
			new AttractionDto(
				Integer.parseInt((String) data.get(ResponseFieldName.CONTENT_ID.getName())),
					Integer.parseInt((String)data.get(ResponseFieldName.CONTENT_TYPE_ID.getName())),
				(String)data.get(ResponseFieldName.TITLE.getName()),
				(String)data.get(ResponseFieldName.ADDR1.getName()),
				(String)data.get(ResponseFieldName.ADDR2.getName()),
				String.valueOf(data.get(ResponseFieldName.TEL.getName())),
				(String)data.get(ResponseFieldName.FIRST_IMAGE.getName()),
				(String)data.get(ResponseFieldName.FIRST_IMAGE2.getName()),
					Double.parseDouble((String)data.get(ResponseFieldName.MAPY.getName())),
					Double.parseDouble((String)data.get(ResponseFieldName.MAPX.getName())),
					(String)data.get(ResponseFieldName.MLEVEL.getName()),
					Integer.parseInt((String)data.get(ResponseFieldName.AREA_CODE.getName())),
					Integer.parseInt((String)data.get(ResponseFieldName.SIGUNGUCODE.getName())),
				(String)data.get(ResponseFieldName.MODIFIED_TIME.getName())
			)).collect(Collectors.toList());
	}

//	private static LocalDateTime convertToLocalDateTime(String date) {
//		StringBuilder sb = new StringBuilder(date.substring(0, 10)).append("T").append(date.substring(11));
//		return LocalDateTime.parse(sb.toString());
//	}

	public int getTotalPage() {
		return getTotalPage(INIT_PER_PAGE);
	}

	public int getTotalPage(int pageSize) {
		return (int)Math.ceil((double)getTotalCount(pageSize) / pageSize);
	}

}