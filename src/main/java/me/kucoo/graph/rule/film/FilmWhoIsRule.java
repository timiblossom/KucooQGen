package me.kucoo.graph.rule.film;

import java.util.Map;
import java.util.Random;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.FactRecord;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import scala.actors.threadpool.Arrays;

public class FilmWhoIsRule extends BaseFilmRule {
	private Random random = new Random();
	private String[] WHO_TYPES = {"Director", 
			                      "Editor", 
			                      "Writer", 
			                      "Cinematographer", 
			                      "Producer", 
			                      "Music contributor", 
			                      "Art director", 
			                      "Costume designer"};


	public FilmWhoIsRule() {
		super();
		type = QuestionType.FILM_WHOIS;
	}
	
	
	protected QuestionEntity generateQuestionFromFilm(FilmEntity film) {
		if (ApplicationContext.getInstace().isDebug)
			System.out.println(film);
		
		int typeIndex = random.nextInt(WHO_TYPES.length);
		
		switch (typeIndex) {
		case 0: //Director
			return generateItemBasedByDirector(film);
		case 1: //Editor
			if (film.editors.size() == 0) {
				return generateItemBasedByDirector(film);
			}
			return generateItemBasedByEditor(film);
		case 2: //Writer
			if (film.writers.size() == 0) {
				return generateItemBasedByDirector(film);
			}
			return generateItemBasedByWriter(film);
		
		case 3: //Cinematographer
			if (film.cinematographers.size() == 0) {
				return generateItemBasedByDirector(film);
			}
			return generateItemBasedByCinematographer(film);
			
		case 4: //Producer
			if (film.producers.size() == 0)
				return generateItemBasedByDirector(film);
			return generateItemBasedByProducer(film);
		
		case 5: //Music contributor
			if (film.musicContributor == null)
				return generateItemBasedByDirector(film);
			return generateItemBasedByMusicContributor(film);
			
		case 6: //Art director
			if (film.artDirector == null)
				return generateItemBasedByDirector(film);
			return generateItemBasedByArtDirector(film);
		
		case 7: //Costume designer
			 if (film.costumeDesigner == null)
				 return generateItemBasedByDirector(film);
			 return generateItemBasedByCostumeDesigner(film);
			
			
		default:
			return generateItemBasedByDirector(film);
		}
	}
	
	
	private QuestionEntity generateItemBasedByCostumeDesigner(FilmEntity film) {

		String name = film.costumeDesigner;
		if (name == null)
			return null;
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
	
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		
		while (who1.toLowerCase().equals(name.toLowerCase())) {
			who1 = names[random.nextInt(names.length)].name;
		}
		
		
		while (name.toLowerCase().equals(who2.toLowerCase()) || who1.toLowerCase().equals(who2.toLowerCase())) {
			who2 = names[random.nextInt(names.length)].name;
		}
		
		while (name.toLowerCase().equals(who3.toLowerCase()) || 
			   who1.toLowerCase().equals(who3.toLowerCase()) || 
			   who2.toLowerCase().equals(who3.toLowerCase())) {
			
			who3 = names[random.nextInt(names.length)].name;
		}
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_WHOIS;
		result.mid = film.costumeDesignerMid;
		result.possibleAnwers = new String[] {name, who1, who2, who3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = name;
		if (film.releaseDate != null)
			result.question = "Who was the costume designer of the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else 
			result.question = "Who was the costume designer of the movie '" + film.title + "'?";
		
		return result;
	}
	
	
	private QuestionEntity generateItemBasedByArtDirector(FilmEntity film) {

		String name = film.artDirector;
		if (name == null)
			return null;
		
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		
		while (who1.toLowerCase().equals(name.toLowerCase())) {
			who1 = names[random.nextInt(names.length)].name;
		}
		
		
		while (name.toLowerCase().equals(who2.toLowerCase()) || who1.toLowerCase().equals(who2.toLowerCase())) {
			who2 = names[random.nextInt(names.length)].name;
		}
		
		while (name.toLowerCase().equals(who3.toLowerCase()) || 
			   who1.toLowerCase().equals(who3.toLowerCase()) || 
			   who2.toLowerCase().equals(who3.toLowerCase())) {
			
			who3 = names[random.nextInt(names.length)].name;
		}
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.artDirectorMid;
		result.type = QuestionType.FILM_WHOIS;
		result.possibleAnwers = new String[] {name, who1, who2, who3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = name;
		if (film.releaseDate != null)
			result.question = "Who was the art director of the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else 
			result.question = "Who was the art director of the movie '" + film.title + "'?";
		
		return result;
	}
	
	
	private QuestionEntity generateItemBasedByMusicContributor(FilmEntity film) {

		String name = film.musicContributor;
		
		if (name == null)
			return null;
		
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		
		while (who1.toLowerCase().equals(name.toLowerCase())) {
			who1 = names[random.nextInt(names.length)].name;
		}
		
		
		while (name.toLowerCase().equals(who2.toLowerCase()) || who1.toLowerCase().equals(who2.toLowerCase())) {
			who2 = names[random.nextInt(names.length)].name;
		}
		
		while (name.toLowerCase().equals(who3.toLowerCase()) || 
			   who1.toLowerCase().equals(who3.toLowerCase()) || 
			   who2.toLowerCase().equals(who3.toLowerCase())) {
			
			who3 = names[random.nextInt(names.length)].name;
		}
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_WHOIS;
		result.mid = film.musicContributorMid;
		result.possibleAnwers = new String[] {name, who1, who2, who3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = name;
		
		if (film.releaseDate != null)
			result.question = "Who was the music contributor of the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Who was the music contributor of the movie '" + film.title + "'?";
		
		return result;
	}
	
	private QuestionEntity generateItemBasedByProducer(FilmEntity film) {
		
		if (film.producers == null || film.producers.size() == 0)
			return null;
		
		String[] producers = film.producers.keySet().toArray(new String[0]);
		Map<String, String> producerSet = film.producers;
		String name = producers[random.nextInt(producers.length)];
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		
		while (who1.toLowerCase().equals(name.toLowerCase()) ||
				producerSet.containsKey(who1)) {
			who1 = names[random.nextInt(names.length)].name;
		}		
		
		while (name.toLowerCase().equals(who2.toLowerCase()) || 
			   who1.toLowerCase().equals(who2.toLowerCase()) ||
			   producerSet.containsKey(who2)) {
			who2 = names[random.nextInt(names.length)].name;
		}
		
		while (name.toLowerCase().equals(who3.toLowerCase()) || 
			   who1.toLowerCase().equals(who3.toLowerCase()) || 
			   who2.toLowerCase().equals(who3.toLowerCase()) ||
			   producerSet.containsKey(who3)) {
			who3 = names[random.nextInt(names.length)].name;
		}
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_WHOIS;
		result.mid = film.producers.get(name);
		result.possibleAnwers = new String[] {name, who1, who2, who3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = name;
		
		if (film.releaseDate != null)
			result.question = "Who was the producer of the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Who was the producer of the movie '" + film.title + "'?";
		
		return result;
	}
	
	
	private QuestionEntity generateItemBasedByCinematographer(FilmEntity film) {
		
		if (film.cinematographers == null || film.cinematographers.size() == 0)
			return null;
			
		String[] cinematographers = film.cinematographers.keySet().toArray(new String[0]);
		Map<String, String> cinematographerSet = film.cinematographers;
		String name = cinematographers[random.nextInt(cinematographers.length)];
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		
		while (who1.toLowerCase().equals(name.toLowerCase()) || cinematographerSet.containsKey(who1)) {
			who1 = names[random.nextInt(names.length)].name;
		}		
		
		while (name.toLowerCase().equals(who2.toLowerCase()) || 
			   who1.toLowerCase().equals(who2.toLowerCase()) || 
			   cinematographerSet.containsKey(who2)) {
			who2 = names[random.nextInt(names.length)].name;
		}
		
		while (name.toLowerCase().equals(who3.toLowerCase()) || 
			   who1.toLowerCase().equals(who3.toLowerCase()) || 
			   who2.toLowerCase().equals(who3.toLowerCase()) || 
			   cinematographerSet.containsKey(who3)) {
			
			who3 = names[random.nextInt(names.length)].name;
		}
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.cinematographers.get(name);
		result.type = QuestionType.FILM_WHOIS;
		
		result.possibleAnwers = new String[] {name, who1, who2, who3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = name;
		if (film.releaseDate != null)
			result.question = "Who was the cinematographer of the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else 
			result.question = "Who was the cinematographer of the movie '" + film.title + "'?";
		
		return result;
	}
	
	private QuestionEntity generateItemBasedByDirector(FilmEntity film) {
		
		if (film.directors == null || film.directors.size() == 0)
			return null;
		
		String[] directors = film.directors.keySet().toArray(new String[0]);
		Map<String, String> directorSet = film.directors;
		String name = directors[random.nextInt(directors.length)];
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		
		while (who1.toLowerCase().equals(name.toLowerCase()) || directorSet.containsKey(who1)) {
			who1 = names[random.nextInt(names.length)].name;
		}		
		
		while (name.toLowerCase().equals(who2.toLowerCase()) || 
			   who1.toLowerCase().equals(who2.toLowerCase()) || 
			   directorSet.containsKey(who2)) {
			who2 = names[random.nextInt(names.length)].name;
		}
		
		while (name.toLowerCase().equals(who3.toLowerCase()) || 
			   who1.toLowerCase().equals(who3.toLowerCase()) || 
			   who2.toLowerCase().equals(who3.toLowerCase()) ||
			   directorSet.containsKey(who3)) {
			
			who3 = names[random.nextInt(names.length)].name;
		}
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.directors.get(name);
		result.type = QuestionType.FILM_WHOIS;
		
		result.possibleAnwers = new String[] {name, who1, who2, who3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = name;
		if (film.releaseDate != null)
			result.question = "Who directed the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else 
			result.question = "Who directed the movie '" + film.title + "'?";
		
		return result;
	}

	
	private QuestionEntity generateItemBasedByEditor(FilmEntity film) {
		
		if (film.editors == null || film.editors.size() == 0)
			return null;
		
		String[] editors = film.editors.keySet().toArray(new String[0]);
		String name = editors[random.nextInt(editors.length)];
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		
		while (who1.toLowerCase().equals(name.toLowerCase())) {
			who1 = names[random.nextInt(names.length)].name;
		}
			
		while (name.toLowerCase().equals(who2.toLowerCase()) || who1.toLowerCase().equals(who2.toLowerCase())) {
			who2 = names[random.nextInt(names.length)].name;
		}
		
		while (name.toLowerCase().equals(who3.toLowerCase()) || 
			   who1.toLowerCase().equals(who3.toLowerCase()) || 
			   who2.toLowerCase().equals(who3.toLowerCase())) {
			
			who3 = names[random.nextInt(names.length)].name;
		}
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.editors.get(name);
		result.type = QuestionType.FILM_WHOIS;
		result.possibleAnwers = new String[] {name, who1, who2, who3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = name;
		if (film.releaseDate != null)
			result.question = "Who was the editor of the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Who was the editor of the movie '" + film.title + "'?";
		
		return result;
	}

	
	private QuestionEntity generateItemBasedByWriter(FilmEntity film) {
		
		if (film.writers == null || film.writers.size() == 0)
			return null;
		
		String[] writers = film.writers.keySet().toArray(new String[0]);
		String name = writers[random.nextInt(writers.length)];
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		
		while (who1.toLowerCase().equals(name.toLowerCase())) {
			who1 = names[random.nextInt(names.length)].name;
		}		
		
		while (name.toLowerCase().equals(who2.toLowerCase()) || who1.toLowerCase().equals(who2.toLowerCase())) {
			who2 = names[random.nextInt(names.length)].name;
		}
		
		while (name.toLowerCase().equals(who3.toLowerCase()) || 
			   who1.toLowerCase().equals(who3.toLowerCase()) || 
			   who2.toLowerCase().equals(who3.toLowerCase())) {
			
			who3 = names[random.nextInt(names.length)].name;
		}
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.writers.get(name);
		result.type = QuestionType.FILM_WHOIS;
		
		result.possibleAnwers = new String[] {name, who1, who2, who3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = name;
		if (film.releaseDate != null)
			result.question = "Who was the writer of the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Who was the writer of the movie '" + film.title + "'?";
		
		return result;
	}
	
	

}
