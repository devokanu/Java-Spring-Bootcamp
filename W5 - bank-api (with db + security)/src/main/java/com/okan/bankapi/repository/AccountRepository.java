package com.okan.bankapi.repository;

import org.springframework.stereotype.Repository;

import com.okan.bankapi.model.Account;

@Repository
public interface AccountRepository {
	
 public void create(String name, 
		 			String surname, 
		 			String email, 
		 			String tc, 
		 			String type);
 
 public Account getDetail(long accountNumber);
 
 public Account deposit(long accountNumber, int depositAmount);
 
 public boolean moneyTransfer(long sender, long receiver, int amount);
 
 public String getLogs(long accountNumber);

 public boolean delete(long accountNumber);
 
 
 
}
