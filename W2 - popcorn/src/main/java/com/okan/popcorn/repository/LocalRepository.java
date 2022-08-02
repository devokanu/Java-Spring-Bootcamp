package com.okan.popcorn.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import org.springframework.stereotype.Repository;

import com.okan.popcorn.model.Movie;

@Repository
public class LocalRepository {
	
	private static Scanner sc;
	
	public void saveMovies(String Title, String Year, String imdbID,String Type, String Poster ) {
		
		try {
			FileWriter fw = new FileWriter("popcornTime.txt",true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println(Title + "," + Year + "," + imdbID + ","+ Type + "," + Poster);
			pw.flush();
			pw.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public Movie getMovie(String id) {
		BufferedReader reader = null;
		Movie mov = null;
		String line = "";
		try {
			
			reader = new BufferedReader(new FileReader("popcornTime.txt"));
			while((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				if(row[2].equals(id)) {
					mov = new Movie();
					mov.setTitle(row[0].toString());
					mov.setYear(row[1].toString());
					mov.setImdbId(row[2].toString());
					mov.setType(row[3].toString());
					mov.setPoster(row[4].toString());
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		return mov;	
	}

}
