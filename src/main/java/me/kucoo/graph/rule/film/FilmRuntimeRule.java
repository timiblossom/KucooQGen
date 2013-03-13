package me.kucoo.graph.rule.film;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import scala.actors.threadpool.Arrays;

public class FilmRuntimeRule extends BaseFilmRule {

	private final static String MINUTES = " minutes";
	
	public FilmRuntimeRule() {
		super();
		type = QuestionType.FILM_RUNTIME;
	}
	
	protected QuestionEntity generateQuestionFromFilm(FilmEntity film) {
		
		if (ApplicationContext.getInstace().isDebug)
			System.out.println(film);

		if (film == null)
			return null;

		if (film.runtime == null)
			return null;

		double dRuntime = Double.parseDouble(film.runtime);
		int runtime = (int) dRuntime;

		int runtime1 = 0;
		int runtime2 = 0;
		int runtime3 = 0;
		int mod = (int) (System.currentTimeMillis() % 3);

		if (mod % 3 == 0) {
			runtime1 = runtime - (random.nextInt(5) + 1) * 10 + random.nextInt(7);
			runtime2 = runtime + (random.nextInt(20) + 1) * 10 + random.nextInt(7);
			runtime3 = runtime + (random.nextInt(20) + 1) * 10 + random.nextInt(7);

			while (runtime2 == runtime3)
				runtime3 = runtime + (random.nextInt(20) + 1) * 10 + random.nextInt(7);
		} else if (mod == 1 ){
			runtime1 = runtime + (random.nextInt(5) + 1) * 10 + random.nextInt(7);
			runtime2 = runtime + (random.nextInt(10) + 1) * 10 + random.nextInt(7);
			runtime3 = runtime + (random.nextInt(20) + 1) * 10 + random.nextInt(7);

			while (runtime1 == runtime2) 
				runtime2 = runtime + (random.nextInt(10) + 1) * 10;

			while (runtime1 == runtime3 || runtime2 == runtime3)
				runtime3 = runtime + (random.nextInt(20) + 1) * 10;

		} else {
			runtime1 = runtime - (random.nextInt(5) + 1) * 10 + random.nextInt(7);
			runtime2 = runtime - (random.nextInt(5) + 1) * 10 + random.nextInt(7);
			runtime3 = runtime + (random.nextInt(20) + 1) * 10 + random.nextInt(7);

			while (runtime1 == runtime2)
				runtime2 = runtime - (random.nextInt(5) + 1) * 10 + random.nextInt(7);
		}


		QuestionEntity result = new QuestionEntity();
		result.mid = film.filmMid;
		result.property = "film_film_cut_runtime";
		result.type = QuestionType.FILM_RUNTIME;
		result.possibleAnwers = new String[] {String.valueOf(runtime1) + MINUTES, 
				                              String.valueOf(runtime2) + MINUTES, 
				                              String.valueOf(runtime3) + MINUTES, 
				                              String.valueOf(runtime)  + MINUTES};
		Arrays.sort(result.possibleAnwers);

		result.answer = String.valueOf(runtime) + MINUTES;
		if (film.releaseDate != null)
			result.question = "What do you think the runtime of the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + " is?";
		else
			result.question = "What do you think the runtime of the movie '" + film.title + "' is?";
		return result;

	}

}
