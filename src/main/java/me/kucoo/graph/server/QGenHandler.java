package me.kucoo.graph.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.FactRecord;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import me.kucoo.graph.rule.RuleFactory;
import me.kucoo.graph.rule.film.BaseFilmRule;
import me.kucoo.graph.thrift.Fact;
import me.kucoo.graph.thrift.Film;
import me.kucoo.graph.thrift.QGen.Iface;
import me.kucoo.graph.thrift.Question;

import org.apache.thrift.TException;

public class QGenHandler implements Iface {
	private static BaseFilmRule rule = (BaseFilmRule)  RuleFactory.getRule(QuestionType.FILM_COMBO);
	private static Set<String> EMPTY_SET = new HashSet<String>();
	private List<Film> popularFilms = null;

	static {
		ApplicationContext.getInstace();
	}

	public QGenHandler() {
		popularFilms = new ArrayList<Film>(ApplicationContext.getInstace().mostPopularFilms.length);
		for(FactRecord fact : ApplicationContext.getInstace().mostPopularFilms) {
			Film film = new Film();
			film.mid = fact.mid;
			film.title = fact.name;
			popularFilms.add(film);
		}


	}

	@Override
	public List<Film> getPopularFilms(String level) throws TException {
		return popularFilms;
	}


	@Override
	public List<Question> getRandomQuestions(short n, List<Film> films)
	throws TException {
		try {
			if (films != null) {
				for(Film film : films) {
					System.out.println(film);
				}
			} else {
				films = popularFilms;
			}

			List<Question> result = new ArrayList<Question>();

			if (films == null || films.size() == 0)
				return result;

			while (result.size() < n) {
				int a = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
				int index = a % films.size();
				Film film = films.get(index);
				if (film.title == null)
					continue;

				QuestionEntity item = rule.generate(film.title);
				Question q = new Question();
				q.answers = new ArrayList<String>(item.possibleAnwers.length);
				short i = 0;
				for(String answer : item.possibleAnwers) {
					q.answers.add(answer);
					if (answer.equals(item.answer)) 
						q.correctAnswer = i;
					i++;
				}	

				q.question = item.question;
				Fact fact = new Fact();
				fact.mid = item.mid;
				fact.property = item.property;
				q.addToFacts(fact);
				result.add(convertQEntityToQuestion(item));
			}


			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TException(e.getMessage());
		}
	}


	@Override
	public List<Question> getQuestions(short n, 
			List<Film> films,
			Set<String> excludedSet) throws TException {
		try {
			for(Film film : films) {
				System.out.println(film);
			}

			List<Question> result = new ArrayList<Question>();
			if (excludedSet == null)
				excludedSet = EMPTY_SET;

			if (films == null || films.size() == 0)
				return result;

			int size = n > films.size() ? n / films.size() : 1;

			for(Film film : films) {
				List<QuestionEntity> qq = rule.generate(film.title, size, excludedSet);
				if (qq != null)
					result.addAll(convertQEntityListToQuestions(qq));
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TException(e.getMessage());
		}
	}


	private List<Question> convertQEntityListToQuestions(List<QuestionEntity> qq ) {
		List<Question> result = new ArrayList<Question>();
		for(QuestionEntity qe : qq) {
			result.add(convertQEntityToQuestion(qe));
		}

		return result;
	}


	private Question convertQEntityToQuestion(QuestionEntity qe) {
		Question q = new Question();
		q.answers = new ArrayList<String>(qe.possibleAnwers.length);
		short i = 0;
		for(String answer : qe.possibleAnwers) {
			q.answers.add(answer);
			if (answer.equals(qe.answer)) 
				q.correctAnswer = i;
			i++;
		}	

		q.question = qe.question;
		Fact fact = new Fact();
		fact.mid = qe.mid;
		fact.property = qe.property;
		q.addToFacts(fact);

		return q;
	}


	public static void main(String[] args) throws TException {
		QGenHandler handler = new QGenHandler();
		List<Film> films = new ArrayList<Film>();
		Film film = new Film();
		film.title = "Gladiator";
		films.add(film);

		Film film1 = new Film();
		film1.title = "Rush Hour";
		films.add(film1);

		/*
		List<Question> qq = handler.getQuestions((short) 10, films, null);
		System.out.println("Size : " + qq.size());

		for(Question q : qq) {
			System.out.println(q);
		}
		 */

		List<Question> qq= handler.getRandomQuestions((short)1, null);
		System.out.println("List size : " + qq.size());
	}




}
