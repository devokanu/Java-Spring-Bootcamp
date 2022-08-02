package com.okan.bankapi.repository;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import com.okan.bankapi.model.Account;
import com.okan.bankapi.model.Account.BalanceType;
import com.okan.bankapi.model.Log;
import com.okan.bankapi.service.ExchangeAPI;

@Mapper
public class BatisAccountRepository implements AccountRepository {
	
	Reader reader;
	private final ExchangeAPI api;
	
	 @Autowired
	 private KafkaTemplate<String, String> producer;

	
	public BatisAccountRepository(ExchangeAPI api) {
		this.api = api;
	}

	private SqlSessionFactory init(Reader reader) {
		try {
			reader = Resources.getResourceAsReader("myBatis_conf.xml");
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

			return sqlSessionFactory;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void create(String name, String surname, String email, String tc, String type) {
		
		Account account = new Account();
		long fileNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		
		account.setName(name);
		account.setSurname(surname);
		account.setEmail(email);
		account.setTc(tc);
		account.setType(BalanceType.valueOf(type));
		account.setNumber(fileNumber);
		account.setBalance(0);
		account.setLastModified(System.currentTimeMillis());
		account.setDeleted(false);
		SqlSession session = init(reader).openSession();
		session.insert("account-mapper.create", account);
		session.commit();
	}

	@Override
	public Account getDetail(long number) {
		SqlSession session = init(reader).openSession();
		Account account = null;

		if (session != null) {
			account = session.selectOne("account-mapper.getDetail", number);
		}
		return account;
	}

	@Override
	public Account deposit(long accountNumber, int depositAmount) {
		SqlSession session = init(reader).openSession();
		Account account = getDetail(accountNumber);
		String message = "";
		
		Map<String, Object> updates = new HashMap<String, Object>();
		updates.put("lastModified", System.currentTimeMillis());
		updates.put("balance", account.getBalance() + depositAmount);
		updates.put("number", accountNumber);

		if (session != null) {
			session.update("account-mapper.update", updates);
			session.commit();
			account = session.selectOne("account-mapper.getDetail", accountNumber);
		}
		
		
		message =  accountNumber + " deposit amount: " + depositAmount + " " + account.getType().toString();
		producer.send("logs", message);
		
		return account;
	}

	@Override
	public boolean moneyTransfer(long sender, long receiver, int amount) {
		SqlSession session = init(reader).openSession();
		int temp = 0;
		Double gold = 0.0;
		String message = "";

		Account senAccount = getDetail(sender);
		Account recAccount = getDetail(receiver);
		
		String type1 = senAccount.getType().toString();
		String type2 = recAccount.getType().toString();
		
		Map<String, Object> senUpdate = new HashMap<String, Object>();
		Map<String, Object> recUpdate = new HashMap<String, Object>();
		
		if(senAccount.getBalance() >= amount) {
			senUpdate.put("lastModified", System.currentTimeMillis());
			senUpdate.put("balance", senAccount.getBalance() - amount);
			senUpdate.put("number", senAccount);
			}
		else {
			return false;
			}
			
		if(senAccount.getType() == recAccount.getType()){
			recUpdate.put("lastModified", System.currentTimeMillis());
			recUpdate.put("balance", recAccount.getBalance() + amount);
			recUpdate.put("number", recAccount);
		}else {
			
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
		
			recUpdate.put("lastModified", System.currentTimeMillis());
			recUpdate.put("balance", recAccount.getBalance() + temp);
			recUpdate.put("number", recAccount);	
		
		
		if (session != null) {
			try {
				session.update("accountMapper.update", senUpdate);
				session.update("accountMapper.update", recUpdate);
				session.commit();
				senAccount = session.selectOne("account-mapper.getDetail", sender);

			} catch (RuntimeException e) {
				session.rollback();
			}

		}
		
		message =  sender + " transfer amount: " + amount + " " + senAccount.getType().toString() + " ,transferred_account: " + receiver ;
		producer.send("logs", message);
		return true;
	}
	
	@Override
	public boolean delete(long accountNumber) {

		SqlSession session = init(reader).openSession();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lastModified", System.currentTimeMillis());
		params.put("number", accountNumber);

		try {
			if (session != null) {
			session.update("account-mapper.deleteAccount", params);
			session.commit();
		}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
		return true;
	}

	
	public String getLogs(long accountNumber) {

		SqlSession session = init(reader).openSession();
		List<Log> logs = new ArrayList<>();
		if (session != null) {
			logs = session.selectList("kafkalog-mapper.getLogs", accountNumber);
		}
		
		StringBuilder sb = new StringBuilder();
		for (Log log : logs) {
			sb.append(log.getMessage()) ;
		}
		return sb.toString();

	}
	
	public void save(String message) {

		String[] logWords = message.split(" ");
		Log log = new Log();
		if (logWords[1] == "transfer") {
			log.setSenderAccount(logWords[0]);
			log.setReceiverAccount(logWords[6]);
			log.setProcess(logWords[1]);
			log.setCurrencyType(logWords[4]);
			log.setAmount(Integer.valueOf(logWords[3]));
			log.setMessage(message);
		} else {

			log.setSenderAccount(logWords[0]);
			log.setReceiverAccount(null);
			log.setProcess(logWords[1]);
			log.setCurrencyType(logWords[4]);
			log.setAmount(Integer.valueOf(logWords[3]));
			log.setMessage(message);
		}

		SqlSession session = init(reader).openSession();
		session.insert("kafkalog-mapper.save", log);
		session.commit();

	}

}
