package com.okan.bankapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.okan.bankapi.dto.AccountAllDetailResponse;
import com.okan.bankapi.dto.AccountCreateRequest;
import com.okan.bankapi.dto.Response;
import com.okan.bankapi.model.Account;
import com.okan.bankapi.repository.AccountRepository;
import com.okan.bankapi.repository.BatisAccountRepository;

@Service
public class AccountService {
	
	private final BatisAccountRepository repo;
	
	@Autowired
	public AccountService(BatisAccountRepository repository) {
		this.repo = repository;
	}
	
	public boolean createAccount(String name, 
 			String surname, 
 			String email, 
 			String tc, 
 			String type) {
		if(!checkType(type)) {
			return false;
		}
		repo.create(name,
	    		surname,
	    		email,
	    		tc,
	    		type);
		return true;
	}
	
	public Account getDetail(long accountNumber) {
		Account last = repo.getDetail(accountNumber);
		return last;
	}
	
	public Account deposit(long accountNumber, int depositAmount) {
		return repo.deposit(accountNumber, depositAmount);
	}
	
	public Response moneyTransfer(long sender, long receiver, int amount) {
		Response res = new Response();
		if(!repo.moneyTransfer(sender, receiver, amount)) {
			res.setSuccess(false);
			res.setMessage("Insufficient balance");
			
		}else {
			res.setSuccess(true);
			res.setMessage("Transferred Successfully");
		}
		
		return res;
		
		
	}
	
	public Response delete(long accountNumber) {
		
		Response res = new Response();
		if(!repo.delete(accountNumber)) {
			res.setSuccess(false);
			res.setMessage("Account delete unsuccessful");
			
		}else {
			res.setSuccess(true);
			res.setMessage("Account delete Successfully");
		}
		
		return res;
		
	}
	
	public String getLogs(long accountNumber){
		
		return repo.getLogs(accountNumber);
	}
	
	public boolean checkType(String accountType) {
		if(accountType == "TRY" || accountType == "USD" || accountType == "GAU" ) {
			return true;
		}
		return false;
	}
	

}
