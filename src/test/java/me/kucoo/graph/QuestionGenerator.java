package me.kucoo.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;

public class QuestionGenerator {
	private static final String DB_PATH = "/tmp/kucoo-db";
	private static final String WRITER_FILE = "./resource/film_writer.result";
	private static final GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );

	/*
	public static void main1(String[] args) {


		IndexManager indexMgr = db.index();
		Index<Node> fulltextValIndex = indexMgr.forNodes("value");
		IndexHits<Node> hits = fulltextValIndex.query("type_object_name", "\"Martin\" AND \"Steve\"");

		Iterator<Node> iterator = hits.iterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			System.out.println("Node id : " + node.getId() + " - node mid : " + node.getProperty("mid"));
			String type = (String) node.getProperty("type_object_type");

			if (type == null || !type.contains("/people/person") || !type.contains("/film/actor"))
				continue;

			System.out.println(node.getId() + " ------------- mid " + node.getProperty("mid") + " -- " + type);

			if (type.contains("/film/actor")) {
				Iterable<Relationship> rels = node.getRelationships();
				Iterator iter = rels.iterator(); 

				while (iter.hasNext()) {
					Relationship rel = (Relationship) iter.next();
					String relName = rel.getType().name();
					if ("/film/actor/film".equals(relName)) {
						System.out.println("\t" + relName + " -- " + rel.getEndNode().getId());
						Node endNode = db.getNodeById(rel.getEndNode().getId());
						Iterator iter1 = endNode.getPropertyKeys().iterator();
						while (iter1.hasNext()) {
							String key = (String) iter1.next();
							System.out.println("\t\t Property[" + key + "] : [" + endNode.getProperty(key) + "]");
						}
					} else
						System.out.println(relName);
				}
			}
		}


	}
	*/

	public static void main(String[] args) {
		Random random = new Random();
		String[] actorNames = LoadUtils.loadNames("./resources/film_actor.result");
		//System.out.println("Actors : " + actorNames.length);
		IndexManager indexMgr = db.index();
		Index<Node> fulltextValIndex = indexMgr.forNodes("value");
		IndexHits<Node> hits = fulltextValIndex.query("type_object_name", "\"Martin\" AND \"Steve\"");
		List<Node> nodes = new ArrayList<Node>();
		Iterator<Node> iterator = hits.iterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			String type = (String) node.getProperty("type_object_type");

			if (type == null || !type.contains("/people/person") || !type.contains("/film/actor"))
				continue;

			System.out.println("Node id : " + node.getId() + " - node mid : " + node.getProperty("mid"));

			nodes.addAll(getNodeFilmsByActorNode(node));
		}	
		/*
			for(Node fNode : nodes) {
				System.out.println("===================================");
				Iterator iter1 = fNode.getPropertyKeys().iterator();
				while (iter1.hasNext()) {
					String key = (String) iter1.next();
					System.out.println("\t\t Property[" + key + "] : [" + fNode.getProperty(key) + "]");
				}
			}
		 */
		if (nodes.size() != 0)  {
			int ranIndex = random.nextInt(nodes.size());
			Film film = new Film();
			Node pickedFileNode = nodes.get(ranIndex);
			//System.out.println("Title : " + pickedFileNode.getProperty("type_object_name"));
			//System.out.println("Initial released date : " + pickedFileNode.getProperty("film_film_initial_release_date")); 
			film.title = (String) pickedFileNode.getProperty("type_object_name");
			film.releaseDate = (String) pickedFileNode.getProperty("film_film_initial_release_date");

			Iterable<Relationship> rels = pickedFileNode.getRelationships();
			Iterator iter = rels.iterator(); 
			//List<Node> genreNodes = new ArrayList<Node>();

			while (iter.hasNext()) {
				Relationship rel = (Relationship) iter.next();
				String relName = rel.getType().name();
				//System.out.println("Relationship : " + rel.getType().name());
				try {
					if ("/film/film/genre".equals(relName)) {
						Node genreNode = rel.getEndNode();
						//genreNodes.add(genreNode);
						film.genres.add((String) genreNode.getProperty("type_object_name"));
						//System.out.println("Genre : " + genreNode.getProperty("type_object_name"));
					} else if ("/film/director/film".equals(relName)) {
						Node directorNode = rel.getStartNode();
						//genreNodes.add(directorNode);
						film.directors.add((String) directorNode.getProperty("type_object_name"));
						//System.out.println("Director : " + directorNode.getProperty("type_object_name"));
					} else if ("/film/editor/film".equals(relName)) {
						Node editorNode = rel.getStartNode();
						//genreNodes.add(editorNode);
						film.editors.add((String) editorNode.getProperty("type_object_name"));
						//System.out.println("Editor : " + editorNode.getProperty("type_object_name"));
					} else if ("/film/producer/film".equals(relName)) {
						Node producerNode = rel.getStartNode();
						//genreNodes.add(producerNode);
						film.producers.add((String) producerNode.getProperty("type_object_name"));
						//System.out.println("Producer : " + producerNode.getProperty("type_object_name"));
					} else if ("/film/writer/film".equals(relName)) {
						Node writerNode = rel.getStartNode();
						//genreNodes.add(writerNode);
						film.writers.add((String) writerNode.getProperty("type_object_name"));
						//System.out.println("Writer : " + writerNode.getProperty("type_object_name"));
					} else if ("/film/cinematographer/film".equals(relName)) {
						Node cinematographerNode = rel.getStartNode();
						//genreNodes.add(cinematographerNode);
						film.cinematographers.add((String) cinematographerNode.getProperty("type_object_name"));
						//System.out.println("Cinematographer : " + cinematographerNode.getProperty("type_object_name"));
					} else if ("/film/film/music".equals(relName)) {
						Node musicNode = rel.getEndNode();
						//genreNodes.add(musicNode);
						//film.musicContributor = (String) musicNode.getProperty("type_object_name");
						//System.out.println("Music : " + musicNode.getProperty("type_object_name"));
					} else if ("/film/music_contributor/film".equals(relName)) {
						Node musicContributerNode = rel.getStartNode();
						//genreNodes.add(musicContributerNode);
						film.musicContributor = (String) musicContributerNode.getProperty("type_object_name");
						//System.out.println("Music Contributer : " + musicContributerNode.getProperty("type_object_name"));
					} else if ("/film/film/runtime".equals(relName)) {
						Node runtimeNode = rel.getStartNode();
						//genreNodes.add(runtimeNode);
						film.runtime = (String) runtimeNode.getProperty("film_film_cut_runtime");
						//System.out.println("Runtime : " + runtimeNode.getProperty("film_film_cut_runtime"));
					} else if ("/film/film/costume_design_by".equals(relName)) {
						Node customeDesignNode = rel.getEndNode();
						//genreNodes.add(customeDesignNode);
						film.customDesigner = (String) customeDesignNode.getProperty("type_object_name");
						//System.out.println("CustomeDesign : " + customeDesignNode.getProperty("type_object_name"));
					} else if ("/film/film/film_art_direction_by".equals(relName)) {
						Node artDirectorNode = rel.getEndNode();
						//genreNodes.add(artDirectorNode);
						film.artDirector = (String) artDirectorNode.getProperty("type_object_name");
						//System.out.println("ArtDirector : " + artDirectorNode.getProperty("type_object_name"));
					} else if ("/film/film/written_by".equals(relName)) {
						Node writtenByNode = rel.getEndNode();
						//genreNodes.add(writtenByNode);
						film.writers.add((String) writtenByNode.getProperty("type_object_name"));
						//System.out.println("Written by : " + writtenByNode.getProperty("type_object_name"));
					} else if ("/film/film/directed_by".equals(relName)) {
						Node directedByNode = rel.getEndNode();
						//genreNodes.add(directedByNode);

						//System.out.println("Directed by : " + directedByNode.getProperty("type_object_name"));
					} else if ("/film/film/featured_song".equals(relName)) {
						Node featuredSongNode = rel.getEndNode();
						//genreNodes.add(featuredSongNode);
						film.featuredSong = (String) featuredSongNode.getProperty("type_object_name");
						//System.out.println("Featured song : " + featuredSongNode.getProperty("type_object_name"));
					} else if ("/film/film/story_by".equals(relName)) {
						Node storyByNode = rel.getEndNode();
						//genreNodes.add(storyByNode);
						film.storyWriter = (String) storyByNode.getProperty("type_object_name");
						//System.out.println("Story by : " + storyByNode.getProperty("type_object_name"));
					} else if ("/film/film/estimated_budget".equals(relName)) {
						Node tNode = rel.getEndNode();
						//genreNodes.add(tNode);
						film.estBudget = (String) tNode.getProperty("measurement_unit_dated_money_value_amount");
						//System.out.println("Est budget : " + tNode.getProperty("measurement_unit_dated_money_value_amount"));
					}


				} catch (org.neo4j.graphdb.NotFoundException e) {
					System.err.println("Not found property : " + e);
				}

			}

			System.out.println(film);
		}



	}

	public static List<Node> getNodeFilmsByActorNode(Node node) {
		String type = (String) node.getProperty("type_object_type");
		if (type == null || !type.contains("/people/person") || !type.contains("/film/actor"))
			return null;

		Iterable<Relationship> rels = node.getRelationships();
		Iterator iter = rels.iterator(); 
		List<Node> result = new ArrayList<Node>();

		while (iter.hasNext()) {
			Relationship rel = (Relationship) iter.next();
			String relName = rel.getType().name();
			if ("/film/actor/film".equals(relName)) {
				//System.out.println("\t" + relName + " -- " + rel.getEndNode().getId());
				Node performanceFilmNode = db.getNodeById(rel.getEndNode().getId());
				Iterator iter1 = performanceFilmNode.getRelationships().iterator();
				while (iter1.hasNext()) {
					Relationship rel1 = (Relationship) iter1.next();

					if ("/film/performance/film".equals(rel1.getType().name())) {
						Node filmNode = rel1.getEndNode();
						result.add(filmNode);
					}
				}

				//result.add(endNode);
				/*
				Iterator iter1 = endNode.getPropertyKeys().iterator();
				while (iter1.hasNext()) {
					String key = (String) iter1.next();
					System.out.println("\t\t Property[" + key + "] : [" + endNode.getProperty(key) + "]");
				}
				 */
			} 
		}

		return result;
	}


	public static class Film {
		public String title;
		public String releaseDate;

		public Set<String> genres = new HashSet<String>();
		public Set<String> directors = new HashSet<String>();
		public Set<String> editors = new HashSet<String>();
		public Set<String> writers = new HashSet<String>();
		public String featuredSong;
		public Set<String> cinematographers = new HashSet<String>();
		public Set<String> producers = new HashSet<String>();
		public String musicContributor;
		public String artDirector;
		public String customDesigner;
		public String storyWriter;
		public String estBudget;
		public String runtime;

		public String toString() {
			StringBuilder sb = new StringBuilder();

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
				for(String s : genres) {
					sb.append("\nGenre : " + s);
				}
			}

			if (directors.size() != 0) {
				for(String s : directors) {
					sb.append("\nDirector : " + s);
				}
			}

			if (editors.size() != 0) {
				for(String s : editors) {
					sb.append("\nEditor : " + s);
				}
			}


			if (writers.size() != 0) {
				for(String s : writers) {
					sb.append("\nWriter : " + s);
				}
			}

			if (featuredSong != null) {
				sb.append("\nFeatured song : " + featuredSong);
			}

			if (cinematographers.size() != 0) {
				for(String s : cinematographers) {
					sb.append("\nCinematographer : " + s);
				}
			}


			if (producers.size() != 0) {
				for(String s : producers) {
					sb.append("\nProducer : " + s);
				}
			}

			if (musicContributor != null) {
				sb.append("\nMusic contributor : " + musicContributor);
			}

			if (artDirector != null) {
				sb.append("\nArt director : " + artDirector);
			}


			if (customDesigner != null) {
				sb.append("\nCustome designer : " + customDesigner);
			}

			if (storyWriter != null) {
				sb.append("\nStory writer : " + storyWriter);
			}

			if (estBudget != null) {
				sb.append("\nEstimated budget : " + estBudget);
			}

			if (runtime != null) {
				sb.append("\nRuntime : " + runtime);
			}

			return sb.toString();
		}

	}
}
