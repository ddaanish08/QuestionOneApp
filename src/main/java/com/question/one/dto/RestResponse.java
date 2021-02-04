package com.question.one.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RestResponse implements Serializable {
	private String taskInfoId;
	private String message;

}
