package com.okan.bankapi.dto.consume;


import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ResultResponse {

	private String base;
	private String lastupdate;
	private DataResponse[] data;
}
