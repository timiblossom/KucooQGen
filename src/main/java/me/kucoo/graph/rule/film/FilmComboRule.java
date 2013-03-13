package me.kucoo.graph.rule.film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import me.kucoo.graph.rule.RuleFactory;

import org.neo4j.graphdb.Node;

public class FilmComboRule extends BaseFilmRule {
	private static Map<QuestionType, Float> WEIGHT_MAP = new HashMap<QuestionType, Float>();
	private WeightedRule[] rules = null;

	static {
		WEIGHT_MAP.put(QuestionType.FILM_WHOIS, .15F);
		WEIGHT_MAP.put(QuestionType.FILM_GENRE, .12F);
		WEIGHT_MAP.put(QuestionType.FILM_RELEASED_DATE, .02F);
		WEIGHT_MAP.put(QuestionType.FILM_BUDGET, .05F);
		WEIGHT_MAP.put(QuestionType.FILM_RUNTIME, .05F);
		WEIGHT_MAP.put(QuestionType.FILM_STARRING_ACTORS, .20F);
		WEIGHT_MAP.put(QuestionType.FILM_TITLE_WHOIS, .12F);
		WEIGHT_MAP.put(QuestionType.FILM_TAGLINE, .10F);
		WEIGHT_MAP.put(QuestionType.FILM_ACTOR_CHARACTER, .19F);
	}

	public FilmComboRule() {
		super();

		final BaseFilmRule[] rawRules = new BaseFilmRule[] {(BaseFilmRule) RuleFactory.getRule(QuestionType.FILM_WHOIS), 
				(BaseFilmRule) RuleFactory.getRule(QuestionType.FILM_GENRE), 
				(BaseFilmRule) RuleFactory.getRule(QuestionType.FILM_RELEASED_DATE), 
				(BaseFilmRule) RuleFactory.getRule(QuestionType.FILM_BUDGET), 
				(BaseFilmRule) RuleFactory.getRule(QuestionType.FILM_RUNTIME), 
				(BaseFilmRule) RuleFactory.getRule(QuestionType.FILM_STARRING_ACTORS),
				(BaseFilmRule) RuleFactory.getRule(QuestionType.FILM_TITLE_WHOIS),
				(BaseFilmRule) RuleFactory.getRule(QuestionType.FILM_TAGLINE),
				(BaseFilmRule) RuleFactory.getRule(QuestionType.FILM_ACTOR_CHARACTER)};

		rules = new WeightedRule[WEIGHT_MAP.size()];
		type = QuestionType.FILM_COMBO;

		int min = 0;
		for(int i=0; i< rawRules.length; i++) {
			rules[i] = new WeightedRule(rawRules[i], min);
			min = rules[i].max + 1;
		}
	}

	private BaseFilmRule getWeightedRule() {
		int index = random.nextInt(100);
		BaseFilmRule rule = null;
		for(WeightedRule wrule : rules) {
			if (wrule.min <= index && index <= wrule.max) {
				rule = wrule.rule;
				break;
			}
		}

		if (rule == null) {
			System.out.println("Random index : " + index);
			debug();
			rule = rules[0].rule; //set rule as the 1st rule if there is a problem
		}
		
		return rule;
	}


	private BaseFilmRule getRuleByWeight() {
		int index = random.nextInt(100);
		BaseFilmRule rule = null;
		for(WeightedRule wrule : rules) {
			if (wrule.min <= index && index <= wrule.max) {
				rule = wrule.rule;
				break;
			}
		}
		return rule;
	}

	/*
	@Override
	public QuestionEntity generate(String keyword) {
		QuestionEntity item = null;
		int count = 0;
		while (item == null && count++ < 10) {
			BaseFilmRule rule = getWeightedRule();

			List<Node> nodes = rule.getNodesFromKeyword(keyword);
			if (nodes == null)
				return null;

			FilmEntity film = getRandomFilm(nodes);
			int subCounter = 0;
			while (film == null && subCounter++ < 5)
				film = rule.getRandomFilm(nodes);

			if (film == null)
				item = null;
			else {
				item = rule.generateQuestionFromFilm(film);
			}
		}

		return item;
	}
	*/
	
	
	public List<QuestionEntity> generate(String keyword, int n) {
		List<QuestionEntity> result = new ArrayList<QuestionEntity>();
		List<Node> nodes = getNodesFromKeyword(keyword);
		Set<String> actorNames = new HashSet<String>();
		Set<String> seenQuestions = new HashSet<String>();
		int colision = 0;
		
		int counter = 0;
		
		while (counter++ < n) {
			FilmEntity film = getRandomFilm(nodes);
			if (film == null)
				return null;
			
			//FilmEntity film = films.get(counter % films.size());
			
			Iterator<ActorRole> iter = film.starringActors.iterator();
			while (iter.hasNext()) {
				ActorRole ar = iter.next();
				if (ar.actor != null)
					actorNames.add(ar.actor);
			}
			
			BaseFilmRule rule = getRuleByWeight();

			if (rule == null) {
				debug();
				rule = rules[0].rule; //set rule as the 1st rule if there is a problem
			} 

			QuestionEntity q = rule.generateQuestionFromFilm(film);
			if (q != null) {
				if (!seenQuestions.contains(q.question)) {
					result.add(q);
					seenQuestions.add(q.question);
					colision++;
				}
			}	
		}
		
		
		if (result.size() < n) {
			//generate more questions from actors in the given films
			String[] aNames = actorNames.toArray(new String[0]);
			while (result.size() < n) {
				int index = random.nextInt(aNames.length);
				String actorName = aNames[index];
				QuestionEntity qe = generate(actorName);
				if (qe != null) {
					if (!seenQuestions.contains(qe.question)) {
						result.add(qe);
						seenQuestions.add(qe.question);
						colision++;
					}
				}	
				
				if (colision > 100)
					System.err.println("Colission!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! on keyword : " + keyword);
			}
		}

		return result;
	}

	
	private void debug() {
		System.out.println("Some problem with the weight assignment!!!!");
		for(WeightedRule wrule : rules) {
			System.out.println("Min : " + wrule.min + " and Max : " + wrule.max);
		}
	}


	private static class WeightedRule {
		public int min;
		public int max;
		public BaseFilmRule rule;

		public WeightedRule(BaseFilmRule newRule, int min) {
			QuestionType type = newRule.getQuestionType();
			float weight = WEIGHT_MAP.get(type);
			this.min = min;
			this.max = min + ((int)(weight * 100)) - 1;
			this.rule = newRule;
		}
	}


	@Override
	protected QuestionEntity generateQuestionFromFilm(FilmEntity film) {
		QuestionEntity item = null;
		int count = 0;
		while (item == null && count++ < 10) {
			BaseFilmRule rule = getWeightedRule();
			item = rule.generateQuestionFromFilm(film);
		}

		return item;
	}


}
