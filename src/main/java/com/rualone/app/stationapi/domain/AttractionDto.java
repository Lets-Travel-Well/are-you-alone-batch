package com.rualone.app.stationapi.domain;

import com.rualone.app.main.entity.AttractionInfo;
import com.rualone.app.main.entity.Gugun;
import com.rualone.app.main.entity.Sido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDto {
	private Integer contentid;
	private Integer contenttypeid;
	private String title;
	private String addr1;
	private String addr2;
	private String tel;
	private String firstimage;
	private String firstimage2;
	private Double mapy;
	private Double mapx;
	private String mlevel;
	private Integer areacode;
	private Integer sigungucode;
	private String modifiedtime;

	public AttractionInfo toEntity(Sido sido, Gugun gugun) {
		return AttractionInfo.builder()
				.contentId(contentid)
				.contentTypeId(contenttypeid)
				.title(title)
				.addr1(addr1)
				.addr2(addr2)
				.tel(tel)
				.firstImage(firstimage)
				.firstImage2(firstimage2)
				.latitude(mapy)
				.longitude(mapx)
				.mlevel(mlevel)
				.sido(sido)
				.gugun(gugun)
				.modifiedTime(modifiedtime)
				.build();
	}

}