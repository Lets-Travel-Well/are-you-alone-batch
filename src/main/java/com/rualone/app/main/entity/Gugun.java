package com.rualone.app.main.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Gugun {
	@Id
	private Integer gugunCode;

	private String gugunName;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "sido_code")
	private Sido sido;
}
