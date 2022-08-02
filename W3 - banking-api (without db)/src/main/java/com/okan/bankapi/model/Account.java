package com.okan.bankapi.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

	private String name;
	private String surname;
	private String email;
	private String tc;
	private BalanceType type;
	private String number;
	private double balance;
	private ZonedDateTime lastModified; 
	
	
	public enum BalanceType{
		TRY,USD,GAU
	}
}


