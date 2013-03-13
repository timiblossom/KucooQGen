package me.kucoo.graph;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class ApplicationContext {
	public final String DB_PATH = "/mnt/kucoo-db";
	public final String ACTOR_FILE = "/mnt/resources/film_actor.result";
	public final String GENRE_FILE = "/mnt/resources/film_genre.result";
	public final String FILM_TITLE_FILE = "/mnt/resources/film_title.result";
	public final String POPULAR_FILM_FILE = "/mnt/resources/film_popular_score.result";
	public  GraphDatabaseService db = null;
	public FactRecord[] actorNames = null;
	public FactRecord[] genres = null;
	public FactRecord[] filmTitles = null;
	public FactRecord[] mostPopularFilms = null;
	public boolean isDebug = true;

	private final static int NUM_POPULAR_FILMS = 300;
	private static ApplicationContext instance = new ApplicationContext();

	private ApplicationContext() {
		init();
	}

	public static ApplicationContext getInstace() {
		return instance;
	}

	public void init() {
		db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
		Runtime.getRuntime().addShutdownHook( new Hook() );
		actorNames = LoadUtils.loadFactRecords(ACTOR_FILE);
		genres = LoadUtils.loadFactRecords(GENRE_FILE);
		filmTitles = LoadUtils.loadFactRecords(FILM_TITLE_FILE);
		mostPopularFilms = LoadUtils.loadFactRecords(POPULAR_FILM_FILE, NUM_POPULAR_FILMS);
		fillMostPopularFilmTitles();
	}


	private void fillMostPopularFilmTitles() {
		for(FactRecord fact : mostPopularFilms) {
			for(FactRecord title : filmTitles) {
				if (fact.mid.equals(title.mid)) {
					fact.name = title.name;
				}
			}
		}
	}


	public void shutdown() {
		db.shutdown();
	}


	private static class Hook extends Thread {

		public void run() {
			System.out.println( "Running Clean Up..." );
			ApplicationContext.getInstace().shutdown();
		}
	}

}
