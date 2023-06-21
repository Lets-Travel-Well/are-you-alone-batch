package com.rualone.app.stationapi.domain;

public enum ResponseFieldName {

	CONTENT_ID("contentid"),
	CONTENT_TYPE_ID("contenttypeid"),
	TITLE("title"),
	ADDR1("addr1"),
	ADDR2("addr2"),
	TEL("tel"),
	FIRST_IMAGE("firstimage"),
	FIRST_IMAGE2("firstimage2"),
	MAPY("mapy"),
	MAPX("mapx"),
	MLEVEL("mlevel"),
	AREA_CODE("areacode"),
	SIGUNGUCODE("sigungucode"),
	MODIFIED_TIME("modifiedtime");

	private String fieldName;

	ResponseFieldName(String fieldName) {
		this.fieldName=fieldName;
	}

	public String getName() {
		return fieldName;
	}
}