package me.kucoo.graph.rule.film;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.FactRecord;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import scala.actors.threadpool.Arrays;

public class FilmActorCharacterRule extends BaseFilmRule {

	public FilmActorCharacterRule() {
		super();
		type = QuestionType.FILM_ACTOR_CHARACTER;
	}
	
	protected QuestionEntity generateQuestionFromFilm(FilmEntity film) {
		
		if (ApplicationContext.getInstace().isDebug)
			System.out.println(film);
		
		if (film == null || film.starringActors.size() == 0)
			return null;
		

		if (film.starringActors == null || film.starringActors.size() == 0)
			return null;
			
		List<ActorRole> actorRoles = new ArrayList<ActorRole>(film.starringActors.size());
		Iterator<ActorRole> iter = film.starringActors.iterator();
		while (iter.hasNext()) {
			ActorRole ar = iter.next();
			if (ar.actor != null && ar.character != null)
				actorRoles.add(ar);
		}	
		
		if (actorRoles.size() > 0 && actorRoles.size() < 4) {
			return generateOneRole(film, actorRoles);
		} else if (actorRoles.size() >= 4) {
			if (random.nextInt(2) == 0) {
				ActorRole acRight = actorRoles.get(random.nextInt(actorRoles.size()));
				return generateFromOneRole(film, acRight);
			} else {
				return generateThreeRightRoles(film, actorRoles);
			}
		} 		
		
		return null;
	}

	/*********************************************************************************
	 * Generate one role from the actor_role list and 3 other names from general list
	 ***********************************************************************************/
	private QuestionEntity generateOneRole(FilmEntity film, List<ActorRole> actorRoles) {
		
		final FactRecord[] names = ApplicationContext.getInstace().actorNames;
		String who1 = names[random.nextInt(names.length)].name;
		String who2 = names[random.nextInt(names.length)].name;
		String who3 = names[random.nextInt(names.length)].name;
		int ranIndex = random.nextInt(actorRoles.size());
		ActorRole actorRole = actorRoles.get(ranIndex);
		
		while (who1.equals(actorRole.actor))
			who1 = names[random.nextInt(names.length)].name;
		
		while (who1.equals(who2) || actorRole.actor.equals(who2))
			who2 = names[random.nextInt(names.length)].name;
		
		while (who1.equals(who3) || who2.equals(who3) || actorRole.actor.equals(who3))
			who3 = names[random.nextInt(names.length)].name;
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_ACTOR_CHARACTER;
		result.mid = actorRole.charactorMid;
		result.possibleAnwers = new String[] {who1, who2, who3, actorRole.actor};
		
		Arrays.sort(result.possibleAnwers);
		result.answer = actorRole.actor;
		if (film.releaseDate != null)
			result.question = "Which of the following actors played as character '" + actorRole.character + "' in the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which of the following actors played as character '" + actorRole.character + "' in the movie '" + film.title + "'?";
		return result;
	}
	
	
	/*********************************************************************************
	 * Generate question from a given role
	 ***********************************************************************************/
	private QuestionEntity generateFromOneRole(FilmEntity film, ActorRole acRight ) {
		List<ActorRole> actorRoles = new ArrayList<ActorRole>(film.starringActors.size());
		Iterator<ActorRole> iter = film.starringActors.iterator();
		while (iter.hasNext()) {
			ActorRole ar = iter.next();
			if (ar.actor != null)
				actorRoles.add(ar);
		}
		
		int actorIndex1 = random.nextInt(actorRoles.size());
		ActorRole ar1 = actorRoles.remove(actorIndex1);
		
		int actorIndex2 = random.nextInt(actorRoles.size());
		ActorRole ar2 = actorRoles.remove(actorIndex2);
		
		int actorIndex3 = random.nextInt(actorRoles.size());
		ActorRole ar3 = actorRoles.remove(actorIndex3);
		
		QuestionEntity result = new QuestionEntity();
		result.mid = acRight.charactorMid;
		result.type = QuestionType.FILM_ACTOR_CHARACTER;
		result.possibleAnwers = new String[] {ar1.actor, ar2.actor, ar3.actor, acRight.actor};
		
		try {
			Arrays.sort(result.possibleAnwers);
		} catch (Exception e) {
			System.out.println(result.possibleAnwers);
			e.printStackTrace();
		}
		result.answer = acRight.actor;
		if (film.releaseDate != null)
			result.question = "Which of the following actors played as character '" + acRight.character + "' in the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which of the following actors played as character '" + acRight.character + "' in the movie '" + film.title + "'?";
		return result;
	}
	
	
	/*********************************************************************************
	 * Generate one role entirely from the actor_role list 
	 ***********************************************************************************/
	private QuestionEntity generateOneRoleFromRoleList(FilmEntity film, List<ActorRole> actorRoles) {

		int actorIndex1 = random.nextInt(actorRoles.size());
		ActorRole ar1 = actorRoles.remove(actorIndex1);
		
		int actorIndex2 = random.nextInt(actorRoles.size());
		ActorRole ar2 = actorRoles.remove(actorIndex2);
		
		int actorIndex3 = random.nextInt(actorRoles.size());
		ActorRole ar3 = actorRoles.remove(actorIndex3);
		
		ActorRole arRight = actorRoles.remove(random.nextInt(actorRoles.size()));
		
		QuestionEntity result = new QuestionEntity();
		result.mid = arRight.charactorMid;
		result.type = QuestionType.FILM_ACTOR_CHARACTER;
		result.possibleAnwers = new String[] {arRight.actor, ar1.actor, ar2.actor, ar3.actor};
		
		Arrays.sort(result.possibleAnwers);
		result.answer = arRight.character;
		if (film.releaseDate != null)
			result.question = "Which of the following actors played as character '" + arRight.character + "' in the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which of the following actors played as character '" + arRight.character + "' in the movie '" + film.title + "'?";
		
		return result;
	}
	
	
	private QuestionEntity generateThreeRightRoles(FilmEntity film, List<ActorRole> actorRoles) {
		int actorIndex1 = random.nextInt(actorRoles.size());
		ActorRole ar1 = actorRoles.remove(actorIndex1);
		
		int actorIndex2 = random.nextInt(actorRoles.size());
		ActorRole ar2 = actorRoles.remove(actorIndex2);
		
		int actorIndex3 = random.nextInt(actorRoles.size());
		ActorRole ar3 = actorRoles.remove(actorIndex3);
		
		ActorRole arRight = actorRoles.remove(random.nextInt(actorRoles.size()));
		
		QuestionEntity result = new QuestionEntity();
		result.type = QuestionType.FILM_ACTOR_CHARACTER;
		result.mid = arRight.charactorMid;
		result.possibleAnwers = new String[] {arRight.character, ar1.character, ar2.character, ar3.character};
		
		Arrays.sort(result.possibleAnwers);
		result.answer = arRight.character;
		if (film.releaseDate != null)
			result.question = "Which of the following roles was played by " + arRight.actor + " in the movie '" + film.title + "' released in " + film.releaseDate.substring(0,4) + "?";
		else
			result.question = "Which of the following roles was played by " + arRight.actor + " in the movie '" + film.title + "'?";
		
		return result;
	}
	
}
