package me.kucoo.graph.rule.film;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.FactRecord;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import scala.actors.threadpool.Arrays;

public class FilmStarringActorRule extends BaseFilmRule {

	public FilmStarringActorRule() {
		super();
		type = QuestionType.FILM_STARRING_ACTORS;
	}
	
	protected QuestionEntity generateQuestionFromFilm(FilmEntity film) {
		
		if (ApplicationContext.getInstace().isDebug)
			System.out.println(film);
		
		if (film == null || film.starringActors.size() == 0)
			return null;
		
		if (random.nextInt(2) == 0) {
			return generateOneRightStarringActor(film);
		} else {
			if (film.starringActors != null && film.starringActors.size() < 3) {
				return generateOneRightStarringActor(film);
			} else if (film.starringActors != null && film.starringActors.size() >= 3) {
				return generateThreeRightStarringActors(film);
			}
			return null;
		}
		
	}

	
	private QuestionEntity generateOneRightStarringActor(FilmEntity film) {
		if (film.starringActors == null || film.starringActors.size() == 0)
			return null;
		
		
		ActorRole[] actorRoles = film.starringActors.toArray(new ActorRole[0]);
		
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		int ranIndex = random.nextInt(actorRoles.length);
		ActorRole actorRole = actorRoles[ranIndex];
		
		while (actorRole.actor == null) {
			ranIndex = random.nextInt(actorRoles.length);
			actorRole = actorRoles[ranIndex];
		}
			
		
		while (who1.equals(actorRole.actor))
			who1 = names[random.nextInt(names.length)].name;
		
		while (who1.equals(who2) || actorRole.actor.equals(who2))
			who2 = names[random.nextInt(names.length)].name;
		
		while (who1.equals(who3) || who2.equals(who3) || actorRole.actor.equals(who3))
			who3 = names[random.nextInt(names.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.mid = actorRole.actorMid;
		
		result.type = QuestionType.FILM_STARRING_ACTORS;
		result.possibleAnwers = new String[] {who1, who2, who3, actorRole.actor};
		
		Arrays.sort(result.possibleAnwers);
		result.answer = actorRole.actor;
		
		if (film.releaseDate != null)
			result.question = "Which of the following actors was starring in the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which of the following actors was starring in the movie '" + film.title + "'?";
		
		return result;
	}
	
	
	private QuestionEntity generateThreeRightStarringActors(FilmEntity film) {
		if (film.starringActors == null || film.starringActors.size() == 0)
			return null;
		
		//ActorRole[] actorRoles = film.starringActors.toArray(new ActorRole[0]);
		Iterator<ActorRole> iter = film.starringActors.iterator();
		List<ActorRole> actorRoles = new ArrayList<ActorRole>();
		while (iter.hasNext()) {
			ActorRole ar = iter.next();
			if (ar.actor != null)
				actorRoles.add(ar);
		}
		
		ActorRole ar1 = actorRoles.remove(random.nextInt(actorRoles.size()));
		ActorRole ar2 = actorRoles.remove(random.nextInt(actorRoles.size()));
		
		while (ar2.actor.equals(ar1.actor)) {
			ar2 = actorRoles.remove(random.nextInt(actorRoles.size()));
		}
		
		ActorRole ar3 = actorRoles.remove(random.nextInt(actorRoles.size()));
		while (ar3.actor.equals(ar1.actor) || ar3.actor.equals(ar2.actor)) {
			ar3 = actorRoles.remove(random.nextInt(actorRoles.size()));
		}
		
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		
		while (who1.equals(ar1.actor) ||
			   who1.equals(ar2.actor) ||
			   who1.equals(ar3.actor))
			who1 = names[random.nextInt(names.length)].name;
		
		
		QuestionEntity result = new QuestionEntity();
		result.mid = ar1.actorMid;
		result.type = QuestionType.FILM_STARRING_ACTORS;
		result.possibleAnwers = new String[] {who1, ar1.actor, ar2.actor, ar3.actor};
		//for(String s : result.possibleAnwers)
		//	System.out.println("=== " + s);
		
		Arrays.sort(result.possibleAnwers);
		result.answer = who1;
		
		if (film.releaseDate != null)
			result.question = "Which of the following actors was NOT starring in the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which of the following actors was NOT starring in the movie '" + film.title + "'?";
		
		return result;
	}
}
