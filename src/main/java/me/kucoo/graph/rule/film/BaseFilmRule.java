package me.kucoo.graph.rule.film;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import me.kucoo.graph.ApplicationContext;
import me.kucoo.graph.QuestionEntity;
import me.kucoo.graph.QuestionType;
import me.kucoo.graph.rule.IRule;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;

public abstract class BaseFilmRule implements IRule {
	protected Random random = new Random();
	protected IndexManager indexMgr = ApplicationContext.getInstace().db.index();
	protected Index<Node> fulltextValIndex = indexMgr.forNodes("value");
	protected QuestionType type = null;


	public QuestionType getQuestionType() {
		return type;
	}
	
	private FilmEntity getRandomFilmEntityFromKeyword(String keyword) {
		List<Node> nodes = getNodesFromKeyword(keyword);
		if (nodes == null)
			return null;
		
		return getRandomFilm(nodes);
	}
	
	public QuestionEntity generate(String keyword) {
		FilmEntity film = getRandomFilmEntityFromKeyword(keyword);
		
		return generateQuestionFromFilm(film);
	}
	
	
	public QuestionEntity generate(String keyword, Set<String> excludedSet) {
		FilmEntity film = getRandomFilmEntityFromKeyword(keyword);
		QuestionEntity qe = generateQuestionFromFilm(film);
		int counter = 0;
		if (qe == null)
			return null;
		String factId = qe.mid + ":" + qe.property;
		while (qe != null && counter++ < 10 && excludedSet.contains(factId)) {
			qe = generateQuestionFromFilm(film);  //TODO: need more work to traverse outside user's GK boundary
			factId = qe.mid + ":" + qe.property;
		}
		
		return qe;
	}
	
	private List<FilmEntity> getFilmEntityListFromKeyword(String keyword) {
		List<Node> nodes = getNodesFromKeyword(keyword);
		if (nodes == null)
			return null;
				
		List<FilmEntity> films = new ArrayList<FilmEntity>(nodes.size());
		
		for(Node node : nodes) {
			FilmEntity film = getFilmInfo(node);
			films.add(film);
		}
		
		return films;
	}
	
	public List<QuestionEntity> generate(String keyword, int n) {
		List<FilmEntity> films = getFilmEntityListFromKeyword(keyword);
		List<QuestionEntity> result = new ArrayList<QuestionEntity>();
		
		int counter = 0;
		while (counter++ < n) {
			FilmEntity film = films.get(counter % films.size());
			QuestionEntity q = generateQuestionFromFilm(film);
			result.add(q);
		}
		
		return result;
	}
	
	
	public List<QuestionEntity> generate(String keyword, int n, Set<String> excludedSet) {
		List<FilmEntity> films = getFilmEntityListFromKeyword(keyword);
		List<QuestionEntity> result = new ArrayList<QuestionEntity>();
		
		int counter = 0;
		while (counter++ < n) {
			FilmEntity film = films.get(counter % films.size());
			QuestionEntity qe = generateQuestionFromFilm(film);
			if (qe != null) {
				String factId = qe.mid;
				if (qe.property != null) 
					factId += ":" + qe.property;
				if (!excludedSet.contains(factId))  //TODO: traverse outside the boundary and make sure we honor to return n items
					result.add(qe);
			}
		}
		
		return result;
	}
	
	
	protected String computeQuery(String keyword) {
		String[] words = keyword.split(" ");
		String queryStr = "";
		for(int i=0; i<words.length; i++) {
			queryStr += "\"" + words[i] + "\"";
			if (i != words.length - 1) {
				queryStr += " AND ";
			}
		}
		
		if (ApplicationContext.getInstace().isDebug) {
			System.out.println("Query : " + queryStr);
			System.out.println("Question type : " + type);
		}
		
		return queryStr;
	}
	
	
	protected List<Node> getNodesFromKeyword(String keyword) {
		IndexHits<Node> hits = fulltextValIndex.query("type_object_name", computeQuery(keyword));
		List<Node> nodes = new ArrayList<Node>();
		Iterator<Node> iterator = hits.iterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			String type = (String) node.getProperty("type_object_type");

			if (type == null)
				continue;

			System.out.println("Node id : " + node.getId() + " - node mid : " + node.getProperty("mid"));
			///type.contains("/film/film")
			//check for film node here such as gladiator node
			
			if (type.contains("/people/person") || type.contains("/film/actor")) {
				nodes.addAll(getNodeFilmsByActorNode(node));
			} else if (type.contains("/film/film")) {
				nodes.add(node);
			}
		}	

		if (nodes.size() == 0)  {
			System.out.println("No info");
			return null;
		}
		
		return nodes;
	}
	
	
	private int computeWeightedRandomIndex(List<Node> nodes) {
		//compute a node randomly with weights
		double[] mins = new double[nodes.size()];
		double[] maxs = new double[nodes.size()];
		mins[0] = 0.0f;
		//System.out.println("Size : " + nodes.size());
		for(int i = 0; i< nodes.size(); i++) {
			Node tNode = nodes.get(i);
			double w = Float.parseFloat((String) tNode.getProperty("weight"));
			if (i>0)
				mins[i] =  maxs[i-1];
			
			maxs[i] = mins[i] + w;
			//System.out.println("Node mid : " + tNode.getProperty("mid") + ", w : " + w + ", min : " + mins[i] + ", max : " + maxs[i]);
		}
		
		double ran = random.nextDouble();
		double positionValue = ran * maxs[nodes.size()-1];

		for(int i = 0; i< nodes.size(); i++) {
			//Node tNode = nodes.get(i);
			if (mins[i] <= positionValue && positionValue < maxs[i]) {
				return i;
			}
		}
		
		return 0;
	}
	
	protected FilmEntity getRandomFilm(List<Node> nodes)  {
		int ranIndex = computeWeightedRandomIndex(nodes);
		Node filmNode = nodes.get(ranIndex);
		return getFilmInfo(filmNode);
	}
	
	
	protected FilmEntity getFilmInfo(Node filmNode)  {

		FilmEntity film = new FilmEntity();
		if (ApplicationContext.getInstace().isDebug)
			System.out.println("Node : " + filmNode.getId());

		film.filmMid = (String) filmNode.getProperty("mid");
		
		try {
			film.title = (String) filmNode.getProperty("type_object_name");
		} catch (Exception e) {
			System.out.println("no tilte for " + filmNode.getId());
			return null;
		}
		
		try {
			film.releaseDate = (String) filmNode.getProperty("film_film_initial_release_date");
		} catch (Exception e) {
			System.out.println("no released date for " + filmNode.getId());
		}
		
		try {
			film.tagline = (String) filmNode.getProperty("film_film_tagline");
 		} catch (Exception e) {
			System.out.println("no tagline for " + filmNode.getId());
		}
		
		
		Iterable<Relationship> rels = filmNode.getRelationships();
		Iterator<Relationship> iter = rels.iterator(); 

		while (iter.hasNext()) {
			Relationship rel = iter.next();
			String relName = rel.getType().name();
			//System.out.println("Relationship : " + rel.getType().name());
			try {
				if ("/film/film/genre".equals(relName)) {
					Node genreNode = rel.getEndNode();
					//genreNodes.add(genreNode);
					film.genres.put((String) genreNode.getProperty("type_object_name"), (String) genreNode.getProperty("mid"));
					//System.out.println("Genre : " + genreNode.getProperty("type_object_name"));
				} else if ("/film/director/film".equals(relName)) {
					Node directorNode = rel.getStartNode();
					//genreNodes.add(directorNode);
					film.directors.put((String) directorNode.getProperty("type_object_name"), (String) directorNode.getProperty("mid"));
					//System.out.println("Director : " + directorNode.getProperty("type_object_name"));
				} else if ("/film/editor/film".equals(relName)) {
					Node editorNode = rel.getStartNode();
					//genreNodes.add(editorNode);
					film.editors.put((String) editorNode.getProperty("type_object_name"), (String) editorNode.getProperty("mid"));
					//System.out.println("Editor : " + editorNode.getProperty("type_object_name"));
				} else if ("/film/producer/film".equals(relName)) {
					Node producerNode = rel.getStartNode();
					//genreNodes.add(producerNode);
					film.producers.put((String) producerNode.getProperty("type_object_name"), (String) producerNode.getProperty("mid"));
					//System.out.println("Producer : " + producerNode.getProperty("type_object_name"));
				} else if ("/film/writer/film".equals(relName)) {
					Node writerNode = rel.getStartNode();
					//genreNodes.add(writerNode);
					film.writers.put((String) writerNode.getProperty("type_object_name"), (String) writerNode.getProperty("mid"));
					//System.out.println("Writer : " + writerNode.getProperty("type_object_name"));
				} else if ("/film/cinematographer/film".equals(relName)) {
					Node cinematographerNode = rel.getStartNode();
					//genreNodes.add(cinematographerNode);
					film.cinematographers.put((String) cinematographerNode.getProperty("type_object_name"), (String) cinematographerNode.getProperty("mid"));
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
					film.musicContributorMid = (String) musicContributerNode.getProperty("mid");
					//System.out.println("Music Contributer : " + musicContributerNode.getProperty("type_object_name"));
				} else if ("/film/film/runtime".equals(relName)) {
					Node runtimeNode = rel.getEndNode();
					//genreNodes.add(runtimeNode);
					film.runtime = (String) runtimeNode.getProperty("film_film_cut_runtime");
					film.runtimeMid = (String) runtimeNode.getProperty("mid");
					film.runtimePropDesc = "film_film_cut_runtime";
					//System.out.println("Runtime : " + runtimeNode.getProperty("film_film_cut_runtime"));
				} else if ("/film/film/costume_design_by".equals(relName)) {
					Node costumeDesignNode = rel.getEndNode();
					film.costumeDesigner = (String) costumeDesignNode.getProperty("type_object_name");
					film.costumeDesignerMid = (String) costumeDesignNode.getProperty("mid");
					//System.out.println("CustomeDesign : " + customeDesignNode.getProperty("type_object_name"));
				} else if ("/film/film/film_art_direction_by".equals(relName)) {
					Node artDirectorNode = rel.getEndNode();
					//genreNodes.add(artDirectorNode);
					film.artDirector = (String) artDirectorNode.getProperty("type_object_name");
					film.artDirectorMid = (String) artDirectorNode.getProperty("mid");
					//System.out.println("ArtDirector : " + artDirectorNode.getProperty("type_object_name"));
				} else if ("/film/film/written_by".equals(relName)) {
					Node writtenByNode = rel.getEndNode();
					//genreNodes.add(writtenByNode);
					film.writers.put((String) writtenByNode.getProperty("type_object_name"), (String) writtenByNode.getProperty("mid"));
					//System.out.println("Written by : " + writtenByNode.getProperty("type_object_name"));
				} else if ("/film/film/directed_by".equals(relName)) {
					Node directedByNode = rel.getEndNode();
					//genreNodes.add(directedByNode);

					//System.out.println("Directed by : " + directedByNode.getProperty("type_object_name"));
				} else if ("/film/film/featured_song".equals(relName)) {
					Node featuredSongNode = rel.getEndNode();
					//genreNodes.add(featuredSongNode);
					film.featuredSong = (String) featuredSongNode.getProperty("type_object_name");
					film.featuredSongMid = (String) featuredSongNode.getProperty("mid");
					//System.out.println("Featured song : " + featuredSongNode.getProperty("type_object_name"));
				} else if ("/film/film/story_by".equals(relName)) {
					Node storyByNode = rel.getEndNode();
					//genreNodes.add(storyByNode);
					film.storyWriter = (String) storyByNode.getProperty("type_object_name");
					film.storyWriterMid = (String) storyByNode.getProperty("mid");
					//System.out.println("Story by : " + storyByNode.getProperty("type_object_name"));
				} else if ("/film/film/estimated_budget".equals(relName)) {
					Node tNode = rel.getEndNode();
					//genreNodes.add(tNode);
					film.estBudget = (String) tNode.getProperty("measurement_unit_dated_money_value_amount");
					film.estBudgetPropDesc = "measurement_unit_dated_money_value_amount";
					//System.out.println("Est budget : " + tNode.getProperty("measurement_unit_dated_money_value_amount"));
				} else if ("/film/film/production_companies".equals(relName)) {
					Node tNode = rel.getEndNode();
					System.out.println("================================= Production company : " + tNode.getProperty("type_object_name"));
				} else if ("/film/film/starring".equals(relName)) {
					ActorRole ar = getStarringActorInFilm(rel.getEndNode());
					if (ar != null) {
						film.starringActors.add(ar);
					}
					
				} 
	

			} catch (org.neo4j.graphdb.NotFoundException e) {
				System.err.println("Not found property : " + e);
				continue;
			}

		}
		
		return film;
	}
	

	protected List<Node> getNodeFilmsByActorNode(Node node) {
		//String type = (String) node.getProperty("type_object_type");
		//if (type == null || !type.contains("/people/person") || !type.contains("/film/actor"))
		//	return null;

		Iterable<Relationship> rels = node.getRelationships();
		Iterator<Relationship> iter = rels.iterator(); 
		List<Node> result = new ArrayList<Node>();

		while (iter.hasNext()) {
			Relationship rel = iter.next();
			String relName = rel.getType().name();
			if ("/film/actor/film".equals(relName)) {
				//System.out.println("\t" + relName + " -- " + rel.getEndNode().getId());
				Node performanceFilmNode = rel.getEndNode();
				Iterator<Relationship> iter1 = performanceFilmNode.getRelationships().iterator();
				
				while (iter1.hasNext()) {
					Relationship rel1 = iter1.next();

					if ("/film/performance/film".equals(rel1.getType().name())) {
						Node filmNode = rel1.getEndNode();
						result.add(filmNode);
					} 
				}
				
			} 
		}

		return result;
	}
	
	
	protected List<Node> getCharacterNodeByActorNode(Node node) {
		//String type = (String) node.getProperty("type_object_type");
		//if (type == null || !type.contains("/people/person") || !type.contains("/film/actor"))
		//	return null;

		Iterable<Relationship> rels = node.getRelationships();
		Iterator<Relationship> iter = rels.iterator(); 
		List<Node> result = new ArrayList<Node>();

		while (iter.hasNext()) {
			Relationship rel = iter.next();
			String relName = rel.getType().name();
			if ("/film/actor/film".equals(relName)) {
				//System.out.println("\t" + relName + " -- " + rel.getEndNode().getId());
				Node performanceFilmNode = rel.getEndNode();
				Iterator<Relationship> iter1 = performanceFilmNode.getRelationships().iterator();
				while (iter1.hasNext()) {
					Relationship rel1 = iter1.next();

					if ("/film/performance/character".equals(rel1.getType().name())) {
						Node charactorNode = rel1.getEndNode();
						result.add(charactorNode);
					}
				}

			} 
		}

		return result;
	}
	
	protected ActorRole getStarringActorInFilm(Node performanceNode) {
		Iterable<Relationship> rels = performanceNode.getRelationships();
		Iterator<Relationship> iter = rels.iterator();
		ActorRole ar = new ActorRole();
		//Node result = null;
		while (iter.hasNext()) {
			Relationship rel = iter.next();
			String relName = rel.getType().name();
			
			if ("/film/performance/actor".equals(relName)) {
				//result = rel.getEndNode();
				try {
					ar.actor = (String) rel.getEndNode().getProperty("type_object_name");
					ar.actorMid = (String) rel.getEndNode().getProperty("mid");
				} catch (Exception e) {
					System.err.println("No object name for " + rel.getEndNode().getId());
				}
			} else if ("/film/performance/character".equals(relName)) {
				try {
					ar.character = (String) rel.getEndNode().getProperty("type_object_name");
					ar.charactorMid = (String) rel.getEndNode().getProperty("mid");
				} catch (Exception e) {
					System.err.println("No object name for " + rel.getEndNode().getId());
				}
			}
		}
		
		return ar;
	}


	protected abstract QuestionEntity generateQuestionFromFilm(FilmEntity film);

}
