package com.okan.popcorn.model;

import java.util.List;

public class MovieResponse {
	
	private boolean success;
	private List<Movie> result;
	
	public List<Movie> getResult() {
		return result;
	}
	public void setResult(List<Movie> result) {
		this.result = result;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

}
