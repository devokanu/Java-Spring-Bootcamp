package com.okan.bankapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.okan.bankapi.dto.AccountAllDetailResponse;
import com.okan.bankapi.dto.AccountBalanceUpdateRequest;
import com.okan.bankapi.dto.AccountCreateRequest;
import com.okan.bankapi.dto.AccountTransferRequest;
import com.okan.bankapi.dto.Response;
import com.okan.bankapi.exception.ApiRequestException;
import com.okan.bankapi.model.Account;
import com.okan.bankapi.repository.AccountRepository;
import com.okan.bankapi.service.AccountService;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {
	
	private final AccountService accountService;
	
	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	
	@PostMapping
    public ResponseEntity<Object> createAccount(
             @RequestBody AccountCreateRequest accountCreateRequest) {
		Response res = new Response();
		if(!accountService.createAccount(accountCreateRequest)) {
			
			
			res.setSuccess(false);
			res.setMessage("Invalid Account Type: " + accountCreateRequest.getType().toString());
			return ResponseEntity.badRequest().body(res);
		}
		
		res.setSuccess(true);
		res.setMessage("Account Created , accountNumber : " + AccountRepository.createdNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
	
	@GetMapping(path = "/{accountNumber}", produces = { MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Object> getDetailByAccountNumber(@PathVariable String accountNumber){
		
		AccountAllDetailResponse detail = accountService.getDetail(accountNumber);
		
		if(detail == null) {
			throw new ApiRequestException("Oops cannot get information about this account !");
		}
		return ResponseEntity
		.ok()
		.lastModified(detail.getLastModified())
        .body(detail);
	}
	
	@PatchMapping("/{accountNumber}")
	public ResponseEntity<Object> deposit(@PathVariable String accountNumber, @RequestBody AccountBalanceUpdateRequest request){
		AccountAllDetailResponse acc ;
		try {
			 acc = accountService.deposit(accountNumber, request.getBalance());
		} catch (Exception e) {
			throw new ApiRequestException("Oops cannot get information about this account !");
		}
		
		return ResponseEntity
				.ok()
		        .body(acc);
	}
	
	@PatchMapping("/transfer/{accountNumber}")
	public ResponseEntity<Object> transfer(@PathVariable String accountNumber, @RequestBody AccountTransferRequest request){
		Response res ;
		try {
			res = accountService.moneyTransfer(accountNumber, request.getTransferredAccountNumber(), request.getAmount());
		if(!res.isSuccess())	{
			return ResponseEntity
					.badRequest()
			        .body(res);
			}
		} catch (Exception e) {
			throw new ApiRequestException("Oops cannot get information about this account !");
		}
		
		return ResponseEntity
				.ok()
		        .body(res);
	}
	
	@CrossOrigin(origins = { "http://localhost:6162" })
	@GetMapping(path = "/logs/{accountNumber}")
	private ResponseEntity<Object> getLogs(
			@PathVariable(name = "accountNumber") String accountNumber) {
		
			String aa = accountService.getLogs(accountNumber);
		
			return ResponseEntity.ok().body(aa);
		
		
	}
	
	
	

}
