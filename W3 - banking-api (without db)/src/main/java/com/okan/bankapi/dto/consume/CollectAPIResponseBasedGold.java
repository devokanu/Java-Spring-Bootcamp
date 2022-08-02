package com.okan.bankapi.dto.consume;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CollectAPIResponseBasedGold {
	
	private boolean success;
	private ResultResponseBasedGold[] result;

}