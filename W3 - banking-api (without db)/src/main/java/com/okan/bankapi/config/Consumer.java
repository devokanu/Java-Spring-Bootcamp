package com.okan.bankapi.config;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
	
	@KafkaListener(topics = "logs", groupId = "logs_group")
	public void listenTransfer(@Payload String message, 
			  @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition
	) {
	    
		saveLogsToFile(message);
	  
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
