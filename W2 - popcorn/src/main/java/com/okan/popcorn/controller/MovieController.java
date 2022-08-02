package com.okan.popcorn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.okan.popcorn.model.Movie;
import com.okan.popcorn.repository.LocalRepository;
import com.okan.popcorn.service.ConsumerService;
import com.okan.popcorn.service.IConsumerService;

@RestController
public class MovieController{
	
	 private final ConsumerService service;

	 @Autowired
	 public MovieController(ConsumerService service) {
	     this.service = service;
	 }
	 
	 @Autowired
	 private LocalRepository local;
	 @Autowired
	 private IConsumerService consumer;
	
	@RequestMapping(path="/movies/search", method = RequestMethod.GET)
	public List<Movie> search(@RequestParam(name ="movie_name") String name) {
	return service.imdbSearchByName(name);
		
	}
	
	@PostMapping("/movies/saveToList/{id}")
    public boolean addToList(@PathVariable(name = "id") String id){
		if(local.getMovie(id) != null) {
		}else {
		 List<Movie> list = consumer.imdbSearchById(id);
		 try {
			 for (Movie movie : list) {
				 local.saveMovies(movie.getTitle(), movie.getYear(), 
						 movie.getImdbID(), movie.getType(), movie.getPoster());
		}
			 } catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		 
		}
		 return true;
			
    }
	
	@PostMapping("/movies/detail/{id}")
    public Movie getDetail(@PathVariable(name = "id") String id){
		Movie mov = new Movie();
		if(local.getMovie(id) != null) {
			mov = local.getMovie(id);
		}else {
			mov = consumer.imdbSearchById(id).get(0);
		}
		return mov;
        
    }
	
	
	
	
}
