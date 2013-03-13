package me.kucoo.graph.rule.film;

import java.util.Calendar;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import scala.actors.threadpool.Arrays;

public class FilmReleasedDateRule extends BaseFilmRule {

	private static int BACK_YEARS = 70;
	private String[] WHO_TYPES = {"Director", 
            "Editor", 
            "Writer", 
            "Cinematographer", 
            "Producer", 
            "Music contributor", 
            "Art director", 
            "Costume designer"};
	
	public FilmReleasedDateRule() {
		super();
		type = QuestionType.FILM_RELEASED_DATE;
	}
	
	protected QuestionEntity generateQuestionFromFilm(FilmEntity film) {
		
		if (ApplicationContext.getInstace().isDebug)
			System.out.println(film);
		
		if (film == null)
			return null;

		if (film.releaseDate == null) 
			return null;


		int filmYear = Integer.parseInt(film.releaseDate.substring(0, 4));

		Calendar c = Calendar.getInstance();
		int currentYear = c.get(Calendar.YEAR);
		int year1 = 0;
		int year2 = 0;
		int year3 = 0;

		if (currentYear == filmYear) {
			year1 = random.nextInt(BACK_YEARS) + 1;
			year2 = random.nextInt(BACK_YEARS) + 1;
			year3 = random.nextInt(BACK_YEARS) + 1;
			while (year1 == year2)
				year2 = random.nextInt(BACK_YEARS) + 1;

			while (year1 == year3 || year2 == year3)
				year3 = random.nextInt(BACK_YEARS) + 1;
			year1 = currentYear - year1;
			year2 = currentYear - year2;
			year3 = currentYear - year3;
		} else {
			year1 = random.nextInt(Math.abs(currentYear - filmYear) + 1) + 1 + filmYear;
			year2 = random.nextInt(BACK_YEARS) + 1;
			year3 = random.nextInt(BACK_YEARS) + 1;

			while (year2 == year3) 
				year3 = random.nextInt(BACK_YEARS) + 1;
			year2 = currentYear - year2;
			year3 = currentYear - year3;
		}
			
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.filmMid;
		result.property = "film_film_initial_release_date";
		result.type = QuestionType.FILM_RELEASED_DATE;
		result.possibleAnwers = new String[] {String.valueOf(year1), String.valueOf(year2), String.valueOf(year3), String.valueOf(filmYear)};
		Arrays.sort(result.possibleAnwers);

		result.answer = String.valueOf(filmYear);
		String extraPart = getExtraPart(film);
		
		if (extraPart == null)
			result.question = "When was the movie '" + film.title + "' released?"; 
		else 
			result.question = "When was the movie '" + film.title + "'," + extraPart + ", released?";
		
		return result;

	}


	private String getExtraPart(FilmEntity film) {
		int typeIndex = random.nextInt(WHO_TYPES.length);
		
		switch (typeIndex) {
		case 0: //Director
			if (film.directors.size() != 0) {
				int ran = random.nextInt(film.directors.size());
				String[] directors = film.directors.keySet().toArray(new String[0]);
				return " directed by " + directors[ran];
			}
		case 1: //Editor
			if (film.editors.size() != 0) {
				int ran = random.nextInt(film.editors.size());
				String[] editors = film.editors.keySet().toArray(new String[0]);
				return " edited by " + editors[ran];
			}

		case 2: //Writer
			if (film.writers.size() != 0) {
				int ran = random.nextInt(film.writers.size());
				String[] writers = film.writers.keySet().toArray(new String[0]);
				return " written by " + writers[ran];
			}
		
		case 3: //Cinematographer
			if (film.cinematographers.size() != 0) {
				int ran = random.nextInt(film.cinematographers.size());
				String[] cinematographers = film.cinematographers.keySet().toArray(new String[0]);
				return " cinematographed by " + cinematographers[ran];
			}
			
		case 4: //Producer
			if (film.producers.size() != 0) {
				int ran = random.nextInt(film.producers.size());
				String[] producers = film.producers.keySet().toArray(new String[0]);
				return " produced by " + producers[ran];
			}
				
		case 5: //Music contributor
			if (film.musicContributor != null) {
				return " music contributed by " + film.musicContributor;
			}
				
		case 6: //Art director
			if (film.artDirector != null) {
				return " art directed by " + film.artDirector;
			}
		case 7: //Costume designer
			 if (film.costumeDesigner != null) {
				 return " costume designed by " + film.costumeDesigner;
			 } else if (film.directors.size() != 0) {
				int ran = random.nextInt(film.directors.size());
				String[] directors = film.directors.keySet().toArray(new String[0]);
				return " directed by " + directors[ran];
			}
			
		default:
			return null;
		}
	}
}
