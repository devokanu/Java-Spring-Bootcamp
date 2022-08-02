package com.okan.bankapi.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.okan.bankapi.dto.consume.CollectAPIResponse;
import com.okan.bankapi.dto.consume.CollectAPIResponseBasedGold;
import com.okan.bankapi.dto.consume.ResultResponseBasedGold;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;


@Service
public class ExchangeAPI {

	@Autowired
	private RestTemplate client;
	
	
	public double moneyExchange(String base, String to, int amount) {

		HttpHeaders headers = new HttpHeaders();
		
		headers.set("content-type", "application/json");
		headers.set("authorization", "apikey 2SfGrvGe2raC1rTZ5ad0yo:5Vz2e0N16gbduukJlZqQF9");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		
		ResponseEntity<CollectAPIResponse> response = client.exchange(
				"https://api.collectapi.com/economy/exchange?int=" + amount + "&to=" + to + "&base=" + base,
				HttpMethod.GET, entity, CollectAPIResponse.class);

		return response.getBody().getResult().getData()[0].getCalculated();
	}
	
	public double goldExchange() {

		HttpHeaders headers = new HttpHeaders();
		
		headers.set("content-type", "application/json");
		headers.set("authorization", "apikey 2SfGrvGe2raC1rTZ5ad0yo:5Vz2e0N16gbduukJlZqQF9");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		
		ResponseEntity<CollectAPIResponseBasedGold> response = client.exchange(
				"https://api.collectapi.com/economy/goldPrice",
				HttpMethod.GET, entity, CollectAPIResponseBasedGold.class);
		ResultResponseBasedGold[] result = response.getBody().getResult();
		
		
		return result[0].getBuying() ;
	}
	
	
	
	
	}
