package com.okan.bankapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransferRequest {
	
	private long transferredAccountNumber;
	private int amount;

}
