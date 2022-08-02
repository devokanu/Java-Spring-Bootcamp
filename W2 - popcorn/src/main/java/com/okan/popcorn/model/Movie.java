package com.okan.popcorn.model;

public class Movie {
	
	private String Title;
	private String Year;
	private String imdbID;
	private String Type;
	private String Poster;
	
	
	
	public Movie(String title, String year, String imdbId, String type, String poster) {
		super();
		Title = title;
		Year = year;
		imdbID = imdbId;
		Type = type;
		Poster = poster;
	}
	public Movie() {
		// TODO Auto-generated constructor stub
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public String getImdbID() {
		return imdbID;
	}
	public void setImdbId(String imdbID) {
		this.imdbID = imdbID;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getPoster() {
		return Poster;
	}
	public void setPoster(String poster) {
		Poster = poster;
	}
	
	

}
