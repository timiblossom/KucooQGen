package me.kucoo.graph.rule.film;

import java.text.NumberFormat;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import scala.actors.threadpool.Arrays;

public class FilmEstBudgetRule extends BaseFilmRule {

	private static long ONE_HUNDRED_MILLIONS = 100000000;
	private static long TEN_MILLIONS = 10000000;
	private static long ONE_MILLIONS = 1000000;
	
	public FilmEstBudgetRule() {
		super();
		type = QuestionType.FILM_BUDGET;
	}
	
	
	protected QuestionEntity generateQuestionFromFilm(FilmEntity film) {
		
		if (ApplicationContext.getInstace().isDebug)
			System.out.println(film);
		
		if (film == null)
			return null;
		
		if (film.estBudget == null)
			return null;
		
		long estBudget = (long) Double.parseDouble(film.estBudget);
		long budget1 = 0;
		long budget2 = 0;
		long budget3 = 0;
		
		if (estBudget <= ONE_MILLIONS) {
			budget1 = estBudget - ((int) (estBudget * 0.1 * (random.nextInt(8) + 1)));
			budget2 = estBudget + (random.nextInt(9) + 1) * ONE_MILLIONS;
			budget3 = estBudget + (random.nextInt(10) + 10) * ONE_MILLIONS;
		} else if (estBudget <= TEN_MILLIONS) {
			budget1 = estBudget + (random.nextInt(10) + 1) * ONE_MILLIONS;
			budget2 = estBudget + (random.nextInt(10) + 1) * ONE_MILLIONS;
			budget3 = Math.abs(estBudget - (random.nextInt(9) + 1) * ONE_MILLIONS);
			
			while (budget1 == budget2)
				budget2 = estBudget + (random.nextInt(10) + 1) * ONE_MILLIONS;
		} else if (estBudget <= ONE_HUNDRED_MILLIONS) {
			budget1 = ONE_HUNDRED_MILLIONS + (random.nextInt(10) + 1) * TEN_MILLIONS;
			budget2 = estBudget + (random.nextInt(10) + 1) * TEN_MILLIONS;
			budget3 = Math.abs(estBudget - (random.nextInt(8) + 1) * TEN_MILLIONS) + (random.nextInt(7) + 1) * ONE_MILLIONS;
			
			while (budget1 == budget2)
				budget2 = estBudget + (random.nextInt(5) + 1) * TEN_MILLIONS;
		} else {
			budget1 = ONE_HUNDRED_MILLIONS + (random.nextInt(30) + 1) * TEN_MILLIONS;
			budget2 = estBudget + (random.nextInt(10) + 1) * TEN_MILLIONS;
			budget3 = Math.abs(estBudget - (random.nextInt(9) + 1) * TEN_MILLIONS);
			
			while (budget1 == budget2)
				budget2 = estBudget + (random.nextInt(5) + 1) * TEN_MILLIONS;
		}
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		
		QuestionEntity result = new QuestionEntity();
		result.mid = film.filmMid;
		result.type = QuestionType.FILM_BUDGET;
		result.possibleAnwers = new String[] {formatter.format(budget1), formatter.format(budget2), formatter.format(budget3), formatter.format(estBudget)};
		
		Arrays.sort(result.possibleAnwers);
		result.answer = formatter.format(estBudget);
		if (film.releaseDate != null)
			result.question = "Which of the following is best to approximate the budget for the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else 
			result.question = "Which of the following is best to approximate the budget for the movie '" + film.title + "'?";
		return result;
		
	}

	
}
