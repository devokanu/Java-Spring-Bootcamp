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
import com.okan.bankapi.dto.Response;
import com.okan.bankapi.model.Account;
import com.okan.bankapi.service.ExchangeAPI;

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
