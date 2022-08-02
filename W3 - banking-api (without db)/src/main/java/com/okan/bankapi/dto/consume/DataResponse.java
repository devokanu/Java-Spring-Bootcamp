package com.okan.bankapi.dto.consume;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class DataResponse {
	private String code;
	private String name;
	private String rate;
	private String calculatedstr;
	private double calculated;
}
