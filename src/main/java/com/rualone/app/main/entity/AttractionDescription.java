package com.rualone.app.main.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class AttractionDescription {
	@Id
	private int contentId;
	private String homepage;
	private String overview;
	private String telname;

}
