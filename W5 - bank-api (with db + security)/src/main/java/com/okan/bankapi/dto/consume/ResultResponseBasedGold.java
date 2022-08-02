package com.okan.bankapi.dto.consume;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ResultResponseBasedGold {
	private String name;
	private double buying;
	private String buyingstr;
	private double selling;
	private String sellingstr;
	private String time;
	private String date;
	private String datetime;
	private double rate;
}
