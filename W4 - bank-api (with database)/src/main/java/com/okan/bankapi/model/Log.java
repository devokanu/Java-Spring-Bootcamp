package com.okan.bankapi.model;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {
	private long id;
	private String senderAccount;
	private String receiverAccount;
	private String process;
	private String currencyType;
	private int amount;
	private String message;
}
