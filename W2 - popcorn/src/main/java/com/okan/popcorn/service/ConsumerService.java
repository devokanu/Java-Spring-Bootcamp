package com.okan.popcorn.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.okan.popcorn.helpers.JsonParser;
import com.okan.popcorn.model.Movie;
import com.okan.popcorn.model.MovieResponse;
import com.okan.popcorn.repository.LocalRepository;

@Service
public class ConsumerService implements IConsumerService {
	
	@Autowired
	private RestTemplate client;
	
	@Autowired
	private JsonParser parser;
	
	@Autowired
	private LocalRepository local;
	
	

	public List<Movie> imdbSearchByName(String name) {
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("content-type", "application/json");
		headers.add("authorization", "apikey 2SfGrvGe2raC1rTZ5ad0yo:5Vz2e0N16gbduukJlZqQF9");
		
		String url = "https://api.collectapi.com/imdb/imdbSearchByName?query=" + name ;
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String r = response.getBody();
		List<Movie> movies = parser.getParsedMovies(r);
		
		return movies;
		}
	
	public List<Movie> imdbSearchById(String id){
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("content-type", "application/json");
		headers.add("authorization", "apikey 2SfGrvGe2raC1rTZ5ad0yo:5Vz2e0N16gbduukJlZqQF9");
		
		String url = "https://api.collectapi.com/imdb/imdbSearchById?movieId=" + id ;
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = client.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String r = response.getBody();
		List<Movie> movies = parser.getParsedMovies(r);
		
		return movies;
	}

	
		
		
	}


