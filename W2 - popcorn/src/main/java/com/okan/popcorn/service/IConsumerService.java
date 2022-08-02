package com.okan.popcorn.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.okan.popcorn.model.Movie;

@Service
public interface IConsumerService extends IMovieService {

	List<Movie> imdbSearchByName(String name);
	List<Movie> imdbSearchById(String name);

}
