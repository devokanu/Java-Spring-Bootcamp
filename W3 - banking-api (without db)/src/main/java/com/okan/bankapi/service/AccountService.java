package com.okan.bankapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.okan.bankapi.dto.AccountAllDetailResponse;
import com.okan.bankapi.dto.AccountCreateRequest;
import com.okan.bankapi.dto.Response;
import com.okan.bankapi.repository.AccountRepository;

@Service
public class AccountService {
	
	private final AccountRepository repo;
	
	@Autowired
	public AccountService(AccountRepository repository) {
		this.repo = repository;
	}
	
	public boolean createAccount(AccountCreateRequest request) {
		if(!checkType(request.getType().toString())) {
			return false;
		}
		repo.createAccount(request);
		return true;
	}
	
	public AccountAllDetailResponse getDetail(String accountNumber) {
		AccountAllDetailResponse last = repo.getDetail(accountNumber);
		return last;
	}
	
	public AccountAllDetailResponse deposit(String accountNumber, double depositAmount) {
		return repo.deposit(accountNumber, depositAmount);
	}
	
	public Response moneyTransfer(String sender, String receiver, double amount) {
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
	
	public String getLogs(String accountNumber){
		
		return repo.getLogs(accountNumber);
	}
	
	public boolean checkType(String accountType) {
		if(accountType == "TRY" || accountType == "USD" || accountType == "GAU" ) {
			return true;
		}
		return false;
	}
	

}
