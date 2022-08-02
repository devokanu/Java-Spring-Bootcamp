package com.okan.bankapi.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String surname;
	private String email;
	private String tc;
	private BalanceType type;
	private long number;
	private int balance;
	private long lastModified;
	private boolean isDeleted;
	
	
	
	
	public enum BalanceType{
		TRY,USD,GAU
	}
}


