package me.kucoo.graph.rule;

import java.util.List;

import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import me.kucoo.graph.rule.film.FilmActorCharacterRule;
import me.kucoo.graph.rule.film.FilmComboRule;
import me.kucoo.graph.rule.film.FilmEstBudgetRule;
import me.kucoo.graph.rule.film.FilmGenreRule;
import me.kucoo.graph.rule.film.FilmReleasedDateRule;
import me.kucoo.graph.rule.film.FilmRuntimeRule;
import me.kucoo.graph.rule.film.FilmStarringActorRule;
import me.kucoo.graph.rule.film.FilmTaglineRule;
import me.kucoo.graph.rule.film.FilmTitleWhoIsRule;
import me.kucoo.graph.rule.film.FilmWhoIsRule;

public class RuleFactory {
	public static IRule getRule(QuestionType type) {
		if (type == QuestionType.FILM_WHOIS) {
			return new FilmWhoIsRule();
		} else if (type == QuestionType.FILM_GENRE) {
			return new FilmGenreRule();
		} else if (type == QuestionType.FILM_RELEASED_DATE) {
			return new FilmReleasedDateRule();
		} else if (type == QuestionType.FILM_BUDGET) {
			return new FilmEstBudgetRule();
		} else if (type == QuestionType.FILM_RUNTIME) {
			return new FilmRuntimeRule();
		} else if (type == QuestionType.FILM_STARRING_ACTORS) {
			return new FilmStarringActorRule();	
		} else if (type == QuestionType.FILM_TITLE_WHOIS) {
			return new FilmTitleWhoIsRule();		
		} else if (type == QuestionType.FILM_TAGLINE) {
			return new FilmTaglineRule();		
		} else if (type == QuestionType.FILM_ACTOR_CHARACTER) {
			return new FilmActorCharacterRule();
		} else if (type == QuestionType.FILM_COMBO) {
			return new FilmComboRule();
		}					
		
		return null;
	}

	
	public static void main(String[] args) {
		IRule rule = RuleFactory.getRule(QuestionType.FILM_STARRING_ACTORS);

		//Scanner input=new Scanner(System.in); 
		System.out.println("Enter something to start");
		
		getNQuestions(rule);
	}
	
	
	private static void getRandomQuestions(IRule rule) {
		int i=0;
		while (i++ < 20) {
		  //System.out.println("Rule : " + rule.getClass());
	  	  //Item item = rule.generate("steve martin");
			
		  //Item item = rule.generate("john travolta");
		  QuestionEntity item = rule.generate("russell crowe");
		  //Question item = rule.generate("big boss");
		  System.out.println(item);
		  
		}
	}
	
	
	private static void getNQuestions(IRule rule) {
		List<QuestionEntity> qq = rule.generate("russell crowe", 30);

		for(QuestionEntity q : qq) {
			System.out.println(q);
		}
		
		System.out.println("Size : " + qq.size());
	}
	
}
