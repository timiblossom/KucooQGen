package me.kucoo.graph.rule.film;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FilmEntity {
    public String filmMid;  
	public String title;
	public String releaseDate;
	public Map<String, String> genres = new HashMap<String, String>();
	public Map<String, String> directors = new HashMap<String, String>();
	public Map<String, String> editors = new HashMap<String, String>();
	public Map<String, String> writers = new HashMap<String, String>();
	public String featuredSong;
	public String featuredSongMid;
	public Map<String, String> cinematographers = new HashMap<String, String>();
	public Map<String, String> producers = new HashMap<String, String>();
	public String musicContributor;
	public String musicContributorMid;
	public String artDirector;
	public String artDirectorMid;
	public String costumeDesigner;
	public String costumeDesignerMid;
	public String storyWriter;
	public String storyWriterMid;
	public String estBudget;
	public String estBudgetPropDesc;
	public String runtime;
	public String runtimeMid;
	public String runtimePropDesc;
	public String tagline;
	public Set<ActorRole> starringActors = new HashSet<ActorRole>();


	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (filmMid != null) {
			sb.append("\nFilm Mid : " + filmMid);
		}
		
		if (title != null) {
			sb.append("\nTitle : " + title);
		}

		if (releaseDate != null) {
			sb.append("\nReleased Date : " + releaseDate);
		}

		if (title != null) {
			sb.append("\nTitle : " + title);
		}

		if (genres.size() != 0) {
			for(Map.Entry<String, String> s : genres.entrySet()) {
				sb.append("\nGenre : " + s.getKey() + ", mid : " + s.getValue());
			}
		}

		if (directors.size() != 0) {
			for(Map.Entry<String, String> s : directors.entrySet()) {
				sb.append("\nDirector : " + s.getKey() + ", mid : " + s.getValue());
			}
		}

		if (editors.size() != 0) {
			for(Map.Entry<String, String> s : editors.entrySet()) {
				sb.append("\nEditor : " + s.getKey() + ", mid : " + s.getValue());
			}
		}


		if (writers.size() != 0) {
			for(Map.Entry<String, String> s : writers.entrySet()) {
				sb.append("\nWriter : " + s.getKey() + ", mid : " + s.getValue());
			}
		}

		if (featuredSong != null) {
			sb.append("\nFeatured song : " + featuredSong + ", mid : " + featuredSongMid);
		}

		if (cinematographers.size() != 0) {
			for(Map.Entry<String, String> s : cinematographers.entrySet()) {
				sb.append("\nCinematographer : " + s.getKey() + ", mid : " + s.getValue());
			}
		}


		if (producers.size() != 0) {
			for(Map.Entry<String, String> s : producers.entrySet()) {
				sb.append("\nProducer : " + s.getKey() + ", mid : " + s.getValue());
			}
		}

		if (musicContributor != null) {
			sb.append("\nMusic contributor : " + musicContributor + ", mid : " + musicContributorMid);
		}

		if (artDirector != null) {
			sb.append("\nArt director : " + artDirector + ", mid : " + artDirectorMid);
		}


		if (costumeDesigner != null) {
			sb.append("\nCustome designer : " + costumeDesigner + ", mid : " + costumeDesignerMid);
		}

		if (storyWriter != null) {
			sb.append("\nStory writer : " + storyWriter + ", mid : " + storyWriterMid);
		}

		if (estBudget != null) {
			sb.append("\nEstimated budget : " + estBudget + ", description : " + estBudgetPropDesc);
		}

		if (runtime != null) {
			sb.append("\nRuntime : " + runtime + ", mid : " + runtimeMid + ", description : " + runtimePropDesc);
		}

		if (starringActors.size() != 0) {
			for(ActorRole ar : starringActors) {
				if (ar.actor != null)
					sb.append("\nStarring Actor : " + ar.actor + ", mid : " + ar.actorMid);
				if (ar.character != null)
					sb.append("\t for Character : " + ar.character + ", mid : " + ar.charactorMid);
			}
		}
		
		if (tagline != null) {
			sb.append("\nTagline : " + tagline + ", description : film_film_tagline");
		}
		
		return sb.toString();
	}

}
