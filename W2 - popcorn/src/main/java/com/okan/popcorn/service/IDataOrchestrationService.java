package com.okan.popcorn.service;

import java.util.List;

import com.okan.popcorn.model.Movie;

public interface IDataOrchestrationService extends IMovieService {

	
	boolean saveMovies(String Title, String Year, String imdbID,String Type, String Poster);
	
	
}
