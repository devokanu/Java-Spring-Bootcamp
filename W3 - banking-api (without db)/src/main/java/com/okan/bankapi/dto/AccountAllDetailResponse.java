package com.okan.bankapi.dto;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

import com.okan.bankapi.model.Account;
import com.okan.bankapi.model.Account.BalanceType;

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
public class AccountAllDetailResponse {

	private String name;
	private String surname;
	private String email;
	private String tc;
	private BalanceType type;
	private String number;
	private double balance;
	private ZonedDateTime lastModified; 
	
}
