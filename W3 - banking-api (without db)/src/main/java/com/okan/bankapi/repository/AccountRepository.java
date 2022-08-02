package com.okan.bankapi.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import com.okan.bankapi.dto.AccountAllDetailResponse;
import com.okan.bankapi.dto.AccountCreateRequest;
import com.okan.bankapi.dto.DtoConverter;
import com.okan.bankapi.model.Account;
import com.okan.bankapi.service.ExchangeAPI;

@Repository
public class AccountRepository {
	
 private final DtoConverter converter;
 private final ExchangeAPI api;
 public static long createdNumber;
 
 @Autowired
 private KafkaTemplate<String, String> producer;
 
 public AccountRepository(DtoConverter converter, ExchangeAPI api) {
	 this.converter = converter;
	 this.api = api;
}

	public void createAccount(AccountCreateRequest request ) {
		
		long fileNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		createdNumber = fileNumber;
		//Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ZonedDateTime date = ZonedDateTime.now().with(LocalTime.MAX);
		
		try (FileOutputStream fos = new FileOutputStream(String.valueOf(fileNumber) + ".txt");
			     ObjectOutputStream oos = new ObjectOutputStream(fos)) {

			    // create a new user object
			    Account acc = new Account(
			    		request.getName(),
			    		request.getSurname(),
			    		request.getEmail(),
			    		request.getTc(),
			    		request.getType(),
			    		String.valueOf(fileNumber),
			    		0,
			    		date);
			    

			    // write object to file
			    oos.writeObject(acc);

			} catch (IOException ex) {
			    ex.printStackTrace();
			}
		
	}
	
	public AccountAllDetailResponse getDetail(String accountNumber) {
		
		AccountAllDetailResponse result = null;
		Account nes = null;
		try(FileInputStream fis = new FileInputStream(accountNumber + ".txt");
			ObjectInputStream ois = new ObjectInputStream(fis)) {
			
			 nes = (Account)ois.readObject();
			
			result = converter.convert(nes);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}
	
	public AccountAllDetailResponse deposit(String accountNumber, double depositAmount) {
		
		Account result = null;
		AccountAllDetailResponse response = null;
		String message = "";
		
		try(FileInputStream fis = new FileInputStream(accountNumber + ".txt");
			ObjectInputStream ois = new ObjectInputStream(fis)) {
			
			result = (Account)ois.readObject();
			result.setBalance(result.getBalance() +  depositAmount);
			result.setLastModified(ZonedDateTime.now().with(LocalTime.MAX));
			
			FileOutputStream fos = new FileOutputStream(String.valueOf(accountNumber) + ".txt");
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(result);
		    oos.close();
		    
		    response = getDetail(accountNumber);
		    message =  accountNumber + " deposit amount:" + depositAmount;
			producer.send("logs", message);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return response;
		
	}
	
	public boolean moneyTransfer(String sender, String receiver, double amount) {
		Account result = null;
		Account result2 = null;
		String message = "";
		double temp = 0;
		double gold = 0;
		
		String type1 = "";
		String type2 = "";
		boolean httpMessage ;
		
		try(FileInputStream fis = new FileInputStream(sender + ".txt");
			ObjectInputStream ois = new ObjectInputStream(fis)) {
			
			result = (Account)ois.readObject();
			type1 = result.getType().toString();
			if(result.getBalance() >= amount) {
				result.setBalance(result.getBalance() -  amount);
				result.setLastModified(ZonedDateTime.now().with(LocalTime.MAX));
				
				FileOutputStream fos = new FileOutputStream(String.valueOf(sender) + ".txt");
			    ObjectOutputStream oos = new ObjectOutputStream(fos);
			    oos.writeObject(result);
			    oos.close();
			}else {
				return false;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try(FileInputStream fis2 = new FileInputStream(receiver + ".txt");
				ObjectInputStream ois2 = new ObjectInputStream(fis2)) {
				
				result2 = (Account)ois2.readObject();
				type2 = result2.getType().toString();
				
				if(result.getType() == result2.getType()) {
					result2.setBalance(result2.getBalance() +  amount);
				}
				else {
					
					if(type2 == "GAU" && type1 == "TRY" ) {
						gold = api.goldExchange();
						temp = amount / gold; 
					}
					else if(type2 == "TRY" && type1 == "GAU") {
						gold = api.goldExchange();
						temp = amount * gold; 
						
					}
					
					else if(type2 == "GAU" && type1 == "USD") {
						gold = api.goldExchange();
						temp = api.moneyExchange(type1, "TRY", amount);
						temp = temp / gold; 
					}
					
					else if(type2 == "USD" && type1 == "GAU") {
						gold = api.goldExchange();
						temp = gold * amount;
						temp = api.moneyExchange("TRY", type2, temp);
						
					}
					else {
						temp = api.moneyExchange(type1, type2, amount);
					}
				
				}
					FileOutputStream fos = new FileOutputStream(String.valueOf(receiver) + ".txt");
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					result2.setBalance(result2.getBalance() +  Double.valueOf(String.format("%,.2f", temp)));
					result2.setLastModified(ZonedDateTime.now().with(LocalTime.MAX));
					oos.writeObject(result2);
					oos.close();
					
					message =  sender + " transfer amount:" + amount + ",transferred_account:" + receiver ;
					producer.send("logs", message);
					
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		return true;
		
		
		
	}
	
	public String getLogs(String accountNumber){
		File file = new File("allLogs.txt");
		List<String> lines;
		List<String> logs = new ArrayList();	
		StringBuilder sb = new StringBuilder();
		try {
			lines = FileUtils.readLines(file, "UTF-8");
			for (String line: lines) {
				if (line != null) {
					if (line.contains(accountNumber)) {
						sb.append("\n \"log\" : "+line);
					}
				}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
}
