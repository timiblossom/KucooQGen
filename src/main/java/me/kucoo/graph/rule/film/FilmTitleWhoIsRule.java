package me.kucoo.graph.rule.film;

import java.util.Random;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.FactRecord;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import scala.actors.threadpool.Arrays;

public class FilmTitleWhoIsRule extends BaseFilmRule {
	private Random random = new Random();
	private String[] WHO_TYPES = {"Director", 
			                      "Editor", 
			                      "Writer", 
			                      "Cinematographer", 
			                      "Producer", 
			                      "Music contributor", 
			                      "Art director", 
			                      "Costume designer"};


	public FilmTitleWhoIsRule() {
		super();
		type = QuestionType.FILM_TITLE_WHOIS;
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
		
		String title = film.title;
		final FactRecord[] titles = ApplicationContext.getInstace().filmTitles;
		
		String title1 = titles[random.nextInt(titles.length)].name;
		String title2 = titles[random.nextInt(titles.length)].name;
		String title3 = titles[random.nextInt(titles.length)].name;
		
		while (title1.toLowerCase().equals(title.toLowerCase()))
			title1 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title2.toLowerCase()) || 
			   title1.toLowerCase().equals(title2.toLowerCase()))
			title2 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title3.toLowerCase()) || 
			   title1.toLowerCase().equals(title3.toLowerCase()) ||
		       title2.toLowerCase().equals(title3.toLowerCase()))
			title3 = titles[random.nextInt(titles.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.costumeDesignerMid;
		
		result.type = QuestionType.FILM_WHOIS;
		result.possibleAnwers = new String[] {title, title1, title2, title3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = title;
		
		if (film.releaseDate != null)
			result.question = "Which movie released in " + film.releaseDate.substring(0,4) + " had " + name + " as its costume designer?";
		else
			result.question = "Which movie had " + name + " as its costume designer?";
		return result;
	}
	
	
	private QuestionEntity generateItemBasedByArtDirector(FilmEntity film) {

		String name = film.artDirector;
		if (name == null)
			return null;
		
		String title = film.title;
		final FactRecord[] titles = ApplicationContext.getInstace().filmTitles;
		
		String title1 = titles[random.nextInt(titles.length)].name;
		String title2 = titles[random.nextInt(titles.length)].name;
		String title3 = titles[random.nextInt(titles.length)].name;
		
		while (title1.toLowerCase().equals(title.toLowerCase()))
			title1 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title2.toLowerCase()) || 
			   title1.toLowerCase().equals(title2.toLowerCase()))
			title2 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title3.toLowerCase()) || 
			   title1.toLowerCase().equals(title3.toLowerCase()) ||
		       title2.toLowerCase().equals(title3.toLowerCase()))
			title3 = titles[random.nextInt(titles.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.artDirectorMid;
		result.type = QuestionType.FILM_WHOIS;
		result.possibleAnwers = new String[] {title, title1, title2, title3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = title;
		if (film.releaseDate != null)
			result.question = "Which movie released in " + film.releaseDate.substring(0,4) + " had " + name + " as its art director?";
		else
			result.question = "Which movie had " + name + " as its art director?";
		return result;
	}
	
	
	private QuestionEntity generateItemBasedByMusicContributor(FilmEntity film) {

		String name = film.musicContributor;
		
		if (name == null)
			return null;
		
		String title = film.title;
		final FactRecord[] titles = ApplicationContext.getInstace().filmTitles;
		
		String title1 = titles[random.nextInt(titles.length)].name;
		String title2 = titles[random.nextInt(titles.length)].name;
		String title3 = titles[random.nextInt(titles.length)].name;
		
		while (title1.toLowerCase().equals(title.toLowerCase()))
			title1 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title2.toLowerCase()) || 
			   title1.toLowerCase().equals(title2.toLowerCase()))
			title2 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title3.toLowerCase()) || 
			   title1.toLowerCase().equals(title3.toLowerCase()) ||
		       title2.toLowerCase().equals(title3.toLowerCase()))
			title3 = titles[random.nextInt(titles.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.artDirectorMid;
		result.type = QuestionType.FILM_WHOIS;
		
		result.possibleAnwers = new String[] {title, title1, title2, title3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = title;
		if (film.releaseDate != null)
			result.question = "Which movie released in " + film.releaseDate.substring(0,4) + " had " + name + " as its music contributor?";
		else 
			result.question = "Which movie had " + name + " as its music contributor?";
		return result;
	}
	
	private QuestionEntity generateItemBasedByProducer(FilmEntity film) {
		
		if (film.producers == null || film.producers.size() == 0)
			return null;
		
		String[] producers = film.producers.keySet().toArray(new String[0]);
		String name = producers[random.nextInt(producers.length)];
		String title = film.title;
		final FactRecord[] titles = ApplicationContext.getInstace().filmTitles;
		
		String title1 = titles[random.nextInt(titles.length)].name;
		String title2 = titles[random.nextInt(titles.length)].name;
		String title3 = titles[random.nextInt(titles.length)].name;
		
		while (title1.toLowerCase().equals(title.toLowerCase()))
			title1 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title2.toLowerCase()) || 
			   title1.toLowerCase().equals(title2.toLowerCase()))
			title2 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title3.toLowerCase()) || 
			   title1.toLowerCase().equals(title3.toLowerCase()) ||
		       title2.toLowerCase().equals(title3.toLowerCase()))
			title3 = titles[random.nextInt(titles.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_WHOIS;
		result.mid = film.producers.get(name);
		
		result.possibleAnwers = new String[] {title, title1, title2, title3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = title;
		if (film.releaseDate != null)
			result.question = "Which movie was produced by " + name + " and released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which movie was produced by " + name + "?";
		return result;
	}
	
	
	private QuestionEntity generateItemBasedByCinematographer(FilmEntity film) {
		
		if (film.cinematographers == null || film.cinematographers.size() == 0)
			return null;
			
		String[] cinematographers = film.cinematographers.keySet().toArray(new String[0]);
		String name = cinematographers[random.nextInt(cinematographers.length)];
		String title = film.title;
		final FactRecord[] titles = ApplicationContext.getInstace().filmTitles;
		
		String title1 = titles[random.nextInt(titles.length)].name;
		String title2 = titles[random.nextInt(titles.length)].name;
		String title3 = titles[random.nextInt(titles.length)].name;
		
		while (title1.toLowerCase().equals(title.toLowerCase()))
			title1 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title2.toLowerCase()) || 
			   title1.toLowerCase().equals(title2.toLowerCase()))
			title2 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title3.toLowerCase()) || 
			   title1.toLowerCase().equals(title3.toLowerCase()) ||
		       title2.toLowerCase().equals(title3.toLowerCase()))
			title3 = titles[random.nextInt(titles.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_WHOIS;
		result.mid = film.cinematographers.get(name);
		
		result.possibleAnwers = new String[] {title, title1, title2, title3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = title;
		if (film.releaseDate != null)
			result.question = "Which movie was cinematographed by " + name + " and released in " + film.releaseDate.substring(0,4) + "?";
		else 
			result.question = "Which movie was cinematographed by " + name + "?";
		
		return result;
	}
	
	private QuestionEntity generateItemBasedByDirector(FilmEntity film) {
		
		if (film.directors == null || film.directors.size() == 0)
			return null;
		
		String[] directors = film.directors.keySet().toArray(new String[0]);
		String name = directors[random.nextInt(directors.length)];
		String title = film.title;
		final FactRecord[] titles = ApplicationContext.getInstace().filmTitles;
		
		String title1 = titles[random.nextInt(titles.length)].name;
		String title2 = titles[random.nextInt(titles.length)].name;
		String title3 = titles[random.nextInt(titles.length)].name;
		
		while (title1.toLowerCase().equals(title.toLowerCase()))
			title1 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title2.toLowerCase()) || 
			   title1.toLowerCase().equals(title2.toLowerCase()))
			title2 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title3.toLowerCase()) || 
			   title1.toLowerCase().equals(title3.toLowerCase()) ||
		       title2.toLowerCase().equals(title3.toLowerCase()))
			title3 = titles[random.nextInt(titles.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_WHOIS;
		result.mid = film.directors.get(name);
		
		result.possibleAnwers = new String[] {title, title1, title2, title3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = title;
		if (film.releaseDate != null)
			result.question = "Which movie was directed by " + name + " and released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which movie was directed by " + name + "?";
		return result;
	}

	
	private QuestionEntity generateItemBasedByEditor(FilmEntity film) {
		
		if (film.editors == null || film.editors.size() == 0)
			return null;
		
		String[] editors = film.editors.keySet().toArray(new String[0]);
		String name = editors[random.nextInt(editors.length)];
		
		String title = film.title;
		final FactRecord[] titles = ApplicationContext.getInstace().filmTitles;
		
		String title1 = titles[random.nextInt(titles.length)].name;
		String title2 = titles[random.nextInt(titles.length)].name;
		String title3 = titles[random.nextInt(titles.length)].name;
		
		while (title1.toLowerCase().equals(title.toLowerCase()))
			title1 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title2.toLowerCase()) || 
			   title1.toLowerCase().equals(title2.toLowerCase()))
			title2 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title3.toLowerCase()) || 
			   title1.toLowerCase().equals(title3.toLowerCase()) ||
		       title2.toLowerCase().equals(title3.toLowerCase()))
			title3 = titles[random.nextInt(titles.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_WHOIS;
		result.mid = film.editors.get(name);
		
		result.possibleAnwers = new String[] {title, title1, title2, title3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = title;
		if (film.releaseDate != null)
			result.question = "Which movie was edited by " + name + " and released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which movie was edited by " + name + "?";
		return result;
	}

	
	private QuestionEntity generateItemBasedByWriter(FilmEntity film) {
		
		if (film.writers == null || film.writers.size() == 0)
			return null;
		
		String[] writers = film.writers.keySet().toArray(new String[0]);
		String name = writers[random.nextInt(writers.length)];
		
		String title = film.title;
		final FactRecord[] titles = ApplicationContext.getInstace().filmTitles;
		
		String title1 = titles[random.nextInt(titles.length)].name;
		String title2 = titles[random.nextInt(titles.length)].name;
		String title3 = titles[random.nextInt(titles.length)].name;
		
		while (title1.toLowerCase().equals(title.toLowerCase()))
			title1 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title2.toLowerCase()) || 
			   title1.toLowerCase().equals(title2.toLowerCase()))
			title2 = titles[random.nextInt(titles.length)].name;
		
		while (title.toLowerCase().equals(title3.toLowerCase()) || 
			   title1.toLowerCase().equals(title3.toLowerCase()) ||
		       title2.toLowerCase().equals(title3.toLowerCase()))
			title3 = titles[random.nextInt(titles.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_WHOIS;
		result.mid = film.writers.get(name);
		
		result.possibleAnwers = new String[] {title, title1, title2, title3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = title;
		if (film.releaseDate != null)
			result.question = "Which movie was written by " + name + " and released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which movie was written by " + name + "?";
		return result;
	}
	
	

}
