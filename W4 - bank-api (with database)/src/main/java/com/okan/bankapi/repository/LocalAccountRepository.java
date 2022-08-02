package com.okan.bankapi.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import com.okan.bankapi.dto.AccountAllDetailResponse;
import com.okan.bankapi.dto.AccountCreateRequest;
import com.okan.bankapi.dto.DtoConverter;
import com.okan.bankapi.model.Account;
import com.okan.bankapi.model.Account.BalanceType;
import com.okan.bankapi.service.ExchangeAPI;

@Repository
public class LocalAccountRepository implements AccountRepository {

	 private final DtoConverter converter;
	 private final ExchangeAPI api;
	 public static long createdNumber;
	 
	 @Autowired
	 private KafkaTemplate<String, String> producer;
	 
	 public LocalAccountRepository(DtoConverter converter, ExchangeAPI api) {
		 this.converter = converter;
		 this.api = api;
	}

		public void create(String name, 
	 			String surname, 
	 			String email, 
	 			String tc, 
	 			String type ) {
			
			long fileNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			createdNumber = fileNumber;
			
			try (FileOutputStream fos = new FileOutputStream(String.valueOf(fileNumber) + ".txt");
				     ObjectOutputStream oos = new ObjectOutputStream(fos)) {

				    // create a new user object
				    Account acc = new Account();
				    acc.setName(name);
				    acc.setSurname(surname);
				    acc.setEmail(email);
				    acc.setTc(tc);
				    acc.setType(BalanceType.valueOf(type));
				    acc.setNumber(fileNumber);;
				    acc.setBalance(0);
				    acc.setLastModified(System.currentTimeMillis());
				    		
				    // write object to file
				    oos.writeObject(acc);

				} catch (IOException ex) {
				    ex.printStackTrace();
				}
			
		}
		
		public Account getDetail(long accountNumber) {
			
			Account result = null;
			
			try(FileInputStream fis = new FileInputStream(String.valueOf(accountNumber) + ".txt");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
				
				 result = (Account)ois.readObject();
				
			//	result = converter.convert(nes);
				
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			return result;
		}
		
		public Account deposit(long accountNumber, int depositAmount) {
			
			Account result = null;
			Account response = null;
			String message = "";
			
			try(FileInputStream fis = new FileInputStream(accountNumber + ".txt");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
				
				result = (Account)ois.readObject();
				result.setBalance(result.getBalance() +  depositAmount);
				result.setLastModified(System.currentTimeMillis());
				
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
		
		public boolean moneyTransfer(long sender, long receiver, int amount) {
			Account result = null;
			Account result2 = null;
			String message = "";
			int temp = 0;
			Double gold = 0.0;
			
			String type1 = "";
			String type2 = "";
			boolean httpMessage ;
			
			try(FileInputStream fis = new FileInputStream(sender + ".txt");
				ObjectInputStream ois = new ObjectInputStream(fis)) {
				
				result = (Account)ois.readObject();
				type1 = result.getType().toString();
				if(result.getBalance() >= amount) {
					result.setBalance(result.getBalance() -  amount);
					result.setLastModified(System.currentTimeMillis());
					
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
							temp = amount / gold.intValue(); 
						}
						else if(type2 == "TRY" && type1 == "GAU") {
							gold = api.goldExchange();
							temp = amount * gold.intValue(); 
							
						}
						
						else if(type2 == "GAU" && type1 == "USD") {
							gold = api.goldExchange();
							Double val = api.moneyExchange(type1, "TRY", amount);
							Double localtemp = (val / gold);
							temp = localtemp.intValue() ; 
						}
						
						else if(type2 == "USD" && type1 == "GAU") {
							gold = api.goldExchange();
							temp = gold.intValue() * amount;
							Double val = api.moneyExchange("TRY", type2, temp);
							temp = val.intValue();
						}
						else {
							Double val = api.moneyExchange(type1, type2, amount);
							temp = val.intValue();
						}
					
					}
						FileOutputStream fos = new FileOutputStream(String.valueOf(receiver) + ".txt");
						ObjectOutputStream oos = new ObjectOutputStream(fos);
						result2.setBalance(result2.getBalance() +  temp);
						result2.setLastModified(System.currentTimeMillis());
						oos.writeObject(result2);
						oos.close();
						
						message =  sender + " transfer amount:" + amount + ",transferred_account:" + receiver ;
						producer.send("logs", message);
						
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			return true;
			
			
			
		}
		
		
		public String getLogs(long accountNumber){
			File file = new File("allLogs.txt");
			List<String> lines;
			List<String> logs = new ArrayList();	
			StringBuilder sb = new StringBuilder();
			try {
				lines = FileUtils.readLines(file, "UTF-8");
				for (String line: lines) {
					if (line != null) {
						if (line.contains(String.valueOf(false))) {
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

		@Override
		public boolean delete(long accountNumber) {
			
			// some code that delete account from file
			return false;
		}
	
}
