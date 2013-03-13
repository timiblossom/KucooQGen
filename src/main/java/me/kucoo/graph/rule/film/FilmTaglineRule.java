package me.kucoo.graph.rule.film;


import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.FactRecord;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import scala.actors.threadpool.Arrays;

public class FilmTaglineRule extends BaseFilmRule {

	private static long ONE_HUNDRED_MILLIONS = 100000000;
	private static long TEN_MILLIONS = 10000000;
	private static long ONE_MILLIONS = 1000000;
	
	public FilmTaglineRule() {
		super();
		type = QuestionType.FILM_TAGLINE;
	}
	
	
	protected QuestionEntity generateQuestionFromFilm(FilmEntity film) {
		
		if (ApplicationContext.getInstace().isDebug)
			System.out.println(film);
		
		if (film == null)
			return null;
		
		if (film.tagline == null)
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
		result.mid = film.filmMid;
		result.property = "film_film_tagline";
		result.type = QuestionType.FILM_TAGLINE;
		
		result.possibleAnwers = new String[] {title, title1, title2, title3};
		Arrays.sort(result.possibleAnwers);
		
		result.answer = title;
		
		if (film.tagline.endsWith("."))
			film.tagline = film.tagline.substring(0, film.tagline.length() - 1);
		film.tagline = film.tagline.replaceAll("\\|", " ");
		result.question = "Which movie has the tagline, '" + film.tagline + "'?";
	
		return result;
		
	}

}
