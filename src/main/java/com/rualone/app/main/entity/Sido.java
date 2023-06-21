package com.rualone.app.main.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class Sido {
	@Id
	private Integer sidoCode;
	@Column()
	private String sidoName;

}
