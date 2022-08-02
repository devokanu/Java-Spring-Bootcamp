package com.okan.popcorn.helpers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.okan.popcorn.model.Movie;

@Component
public class JsonParser {
	
	public List<Movie> getParsedMovies(String r){
		
		List<Movie> movies = new ArrayList<Movie>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			
			JsonNode node = mapper.readTree(r);
			JsonNode resultNode = node.get("result");
			
			if(resultNode.isArray()) {
				ArrayNode moviesNode = (ArrayNode) resultNode;
				for(int i = 0; i < moviesNode.size(); i++) {
					JsonNode singleMovie = moviesNode.get(i);
					String title = singleMovie.get("Title").toString();
					String year = singleMovie.get("Year").toString();
					String imdbID = singleMovie.get("imdbID").toString();
					String type = singleMovie.get("Type").toString();
					String poster = singleMovie.get("Poster").toString();
					
					Movie m = new Movie();
					m.setImdbId(imdbID);
					m.setPoster(poster);
					m.setTitle(title);
					m.setType(type);
					m.setYear(year);
					movies.add(m);
				}
			}else {
				
				int titleL = resultNode.get("Title").toString().length();
				int yearL = resultNode.get("Year").toString().length();
				int imdbIDL = resultNode.get("imdbID").toString().length();
				int typeL = resultNode.get("Type").toString().length();
				int posterL = resultNode.get("Poster").toString().length();
				
				String title = resultNode.get("Title").toString().substring(1, titleL - 1);
				String year = resultNode.get("Year").toString().substring(1, yearL - 1);
				String imdbID = resultNode.get("imdbID").toString().substring(1, imdbIDL - 1);
				String type = resultNode.get("Type").toString().substring(1, typeL - 1);
				String poster = resultNode.get("Poster").toString().substring(1, posterL - 1);
				
				Movie m = new Movie();
				m.setImdbId(imdbID);
				m.setPoster(poster);
				m.setTitle(title);
				m.setType(type);
				m.setYear(year);
				movies.add(m);
			}

         	}
		catch (Exception e) {
			// TODO: handle exception
		}
		return movies;
}
	
	

	
}
