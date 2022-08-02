package com.okan.bankapi.dto;

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
public class AccountCreateRequest {

	private String name;
	private String surname;
	private String email;
	private String tc;
	private String type;
	
}
