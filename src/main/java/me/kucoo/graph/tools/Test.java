package me.kucoo.graph.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


public class Test {

	private static Map<String, Integer> FILM_SCORES = new HashMap<String, Integer>();
	
	public static void main(String[] args) throws IOException  {
		File data = new File("./resources/film_popular_score.result");
		BufferedReader bf = new BufferedReader(new FileReader(data));
		
		String line;
		long total = 0;
		while ((line = bf.readLine()) != null) {
			System.out.println("Line : " + line);
			String[] parts = line.split("\t");
			int score = Integer.parseInt(parts[1]);
			total += score;
			FILM_SCORES.put(parts[0], score);
		}
		
		System.out.println("Score : " + FILM_SCORES.get("/m/0cgzk3z") + ", total : " + total + ", weight : " + 
				            new DecimalFormat("#.########").format(((float)FILM_SCORES.get("/m/0cgzk3z"))/total));
	}
	
   
	

}