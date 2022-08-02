package com.okan.popcorn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.okan.popcorn.model.Movie;
import com.okan.popcorn.repository.LocalRepository;

@Service
public class DataOrchestrationService implements IDataOrchestrationService {

	
	@Autowired
	private LocalRepository local;
	
	

	@Override
	public boolean saveMovies(String Title, String Year, String imdbID, String Type, String Poster) {
		local.saveMovies(Title, Year, imdbID, Type, Poster);
		return true;
		
	}



	

}
