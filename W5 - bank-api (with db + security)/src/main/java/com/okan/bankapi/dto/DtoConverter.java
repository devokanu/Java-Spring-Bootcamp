package com.okan.bankapi.dto;

import org.springframework.stereotype.Component;

import com.okan.bankapi.model.Account;

@Component
public class DtoConverter {
	
	public AccountAllDetailResponse convert(Account account) {
		return AccountAllDetailResponse.builder()
				.name(account.getName())
				.surname(account.getSurname())
				.email(account.getEmail())
				.tc(account.getTc())
				.balance(account.getBalance())
				.number(account.getNumber())
				.type(account.getType())
				.lastModified(account.getLastModified())
				.build();
				
	}

}
