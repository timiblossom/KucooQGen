package me.kucoo.graph.rule.film;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.FactRecord;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import scala.actors.threadpool.Arrays;

public class FilmGenreRule extends BaseFilmRule {	
	
	public FilmGenreRule() {
		super();
		type = QuestionType.FILM_GENRE;
	}
	
	
	protected QuestionEntity generateQuestionFromFilm(FilmEntity film) {
		
		if (ApplicationContext.getInstace().isDebug)
			System.out.println(film);
		
		if (film.genres.size() != 0) {
			//String[] genres = film.genres.toArray(new String[0]);
			List<FactRecord> genres = new ArrayList<FactRecord>();
			Iterator<Map.Entry<String, String>> iter = film.genres.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> entry = iter.next();
				FactRecord fact = new FactRecord();
				fact.name = entry.getKey();
				fact.mid = entry.getValue();
				genres.add(fact);
			}
			
			FactRecord genre = genres.get(random.nextInt(genres.size()));
			FactRecord[] allGenres = ApplicationContext.getInstace().genres;
			FactRecord genre1 = allGenres[random.nextInt(allGenres.length)];
			FactRecord genre2 = allGenres[random.nextInt(allGenres.length)];
			FactRecord genre3 = allGenres[random.nextInt(allGenres.length)];
			
			while (genre.name.equals(genre1.name)) {
				genre1 = allGenres[random.nextInt(allGenres.length)];
			}
			
			while (genre.name.equals(genre2.name) || genre1.equals(genre2)) {
				genre2 = allGenres[random.nextInt(allGenres.length)];
			}
			
			while (genre.name.equals(genre3.name) || genre1.equals(genre3) || genre2.equals(genre3)) {
				genre3 = allGenres[random.nextInt(allGenres.length)];
			}
			
			QuestionEntity result = new QuestionEntity();
			result.mid = genre.mid;
			result.type = QuestionType.FILM_GENRE;
			result.possibleAnwers = new String[] {genre.name, genre1.name, genre2.name, genre3.name};
			Arrays.sort(result.possibleAnwers);
			result.answer = genre.name;
			if (film.releaseDate != null)
				result.question = "Which genre does the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + " belong?";
			else 
				result.question = "Which genre does the movie '" + film.title + "' belong?";
			return result;
		}
		
		return null;
	}
	
	
}
