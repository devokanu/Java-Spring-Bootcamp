package com.okan.bankapi.config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.okan.bankapi.model.Log;
import com.okan.bankapi.repository.BatisAccountRepository;

@Component
public class Consumer {
	
	Reader reader;
	private final BatisAccountRepository repo;
	
	
	public Consumer(BatisAccountRepository repo) {
		this.repo = repo;
	}

	@KafkaListener(topics = "logs", groupId = "logs_group")
	public void listenTransfer(@Payload String message, 
			  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition
	) {
	    
		//saveLogsToFile(message);
		repo.save(message);
	  
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
	
	public void saveLogsToFile(String message) {
		FileWriter fw;
		try {
			fw = new FileWriter("allLogs.txt",true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println(message);
			pw.flush();
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	

}
