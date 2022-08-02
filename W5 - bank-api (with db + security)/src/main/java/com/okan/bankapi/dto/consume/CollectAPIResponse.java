package com.okan.bankapi.dto.consume;


import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CollectAPIResponse {
	
	private boolean success;
	private ResultResponse result;

}
