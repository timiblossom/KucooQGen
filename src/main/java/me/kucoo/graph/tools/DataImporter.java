package me.kucoo.graph.tools;



import static org.neo4j.index.impl.lucene.LuceneIndexImplementation.EXACT_CONFIG;
import static org.neo4j.index.impl.lucene.LuceneIndexImplementation.FULLTEXT_CONFIG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import me.kucoo.graph.tools.Report;

import org.neo4j.graphdb.RelationshipType;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndex;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.neo4j.unsafe.batchinsert.LuceneBatchInserterIndexProvider;

public class DataImporter {
	private static Report report;
	private BatchInserter db;
	private BatchInserterIndexProvider lucene;
	private Map<String, Long> MID2ID_MAP = new HashMap<String, Long>();
	private Map<String, Integer> FILM_SCORES = new HashMap<String, Integer>();
	private long totalScores = 0;
	
	public DataImporter(File graphDb) {
		Map<String, String> config = new HashMap<String, String>();
		try {
			if (new File("batch.properties").exists()) {
				System.out.println("Using Existing Configuration File");
			} else {
				System.out.println("Writing Configuration File to batch.properties");
				FileWriter fw = new FileWriter( "batch.properties" );
				fw.append( "use_memory_mapped_buffers=true\n"
						+ "neostore.nodestore.db.mapped_memory=100M\n"
						+ "neostore.relationshipstore.db.mapped_memory=500M\n"
						+ "neostore.propertystore.db.mapped_memory=1G\n"
						+ "neostore.propertystore.db.strings.mapped_memory=200M\n"
						+ "neostore.propertystore.db.arrays.mapped_memory=0M\n"
						+ "neostore.propertystore.db.index.keys.mapped_memory=15M\n"
						+ "neostore.propertystore.db.index.mapped_memory=15M" );
				fw.close();
			}

			config = MapUtil.load( new File(
			"batch.properties" ) );

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		db = createBatchInserter(graphDb, config);
		lucene = createIndexProvider();
		report = createReport();
	}

	protected StdOutReport createReport() {
		return new StdOutReport(10 * 1000 * 1000, 100);
	}

	protected LuceneBatchInserterIndexProvider createIndexProvider() {
		return new LuceneBatchInserterIndexProvider(db);
	}

	protected BatchInserter createBatchInserter(File graphDb, Map<String, String> config) {
		return BatchInserters.inserter(graphDb.getAbsolutePath(), config);
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 3) {
			System.err.println("Usage java -jar batchimport.jar datafile scorefile dboutpath ");
		}
		
		File dataFile = new File(args[0]);
		File filmScoreFile = new File(args[1]);
		File outDbLocation = new File(args[2]);

		if (outDbLocation.exists()) {
			FileUtils.deleteRecursively(outDbLocation);
		}

		DataImporter importBatch = new DataImporter(outDbLocation);
		try {
			if (dataFile.exists() && filmScoreFile.exists()) {
				importBatch.importFilmScores(new FileReader(filmScoreFile), new FileReader(dataFile));
				importBatch.importNodes(new FileReader(dataFile));
				importBatch.importRelationships(new FileReader(dataFile));
			}

			String indexName = "value";
			BatchInserterIndex index = importBatch.nodeIndexFor(indexName, "fulltext");
			importBatch.importIndexName(indexName, index, new FileReader(dataFile));

			indexName = "mid";
			index = importBatch.nodeIndexFor(indexName, "exact");
			importBatch.importIndexMid(indexName, index, new FileReader(dataFile));

		} finally {
			importBatch.finish();
		}
	}

	void finish() {
		lucene.shutdown();
		db.shutdown();
		report.finish();
	}


	static class StdOutReport implements Report {
		private final long batch;
		private final long dots;
		private long count;
		private long total = System.currentTimeMillis(), time, batchTime;

		public StdOutReport(long batch, int dots) {
			this.batch = batch;
			this.dots = batch / dots;
		}

		@Override
		public void reset() {
			count = 0;
			batchTime = time = System.currentTimeMillis();
		}

		@Override
		public void finish() {
			System.out.println("\nTotal import time: "+ (System.currentTimeMillis() - total) / 1000 + " seconds ");
		}

		@Override
		public void dots() {
			if ((++count % dots) != 0) return;
			System.out.print(".");
			if ((count % batch) != 0) return;
			long now = System.currentTimeMillis();
			System.out.println(" "+ (now - batchTime) + " ms for "+batch);
			batchTime = now;
		}

		@Override
		public void finishImport(String type) {
			System.out.println("\nImporting " + count + " " + type + " took " + (System.currentTimeMillis() - time) / 1000 + " seconds ");
		}
	}


	void importFilmScores(Reader scoreReader, Reader dataReader) throws IOException {
		String line;
		BufferedReader dataBufferedReader = new BufferedReader(dataReader);
		BufferedReader bf = new BufferedReader(scoreReader);
		
		report.reset();
		while ((line = bf.readLine()) != null) {
			//System.out.println("Line : " + line);
			String[] parts = line.split("\t");
			int score = Integer.parseInt(parts[1]);
			totalScores += score;
			FILM_SCORES.put(parts[0], score);
		}

		while ((line = dataBufferedReader.readLine()) != null)  {
			String[] parts = line.split("\t");
			if (FILM_SCORES.get(parts[0]) == null) {
				FILM_SCORES.put(parts[0], Integer.valueOf(1));
				totalScores += 1;
			}
		}
		
	}

	void importNodes(Reader reader) throws IOException {
		BufferedReader bf = new BufferedReader(reader);
		Map<String, Object> properties = new HashMap<String, Object>();
		String line;
		String currentMid = null;
		long counter = 0;

		report.reset();
		while ((line = bf.readLine()) != null) {
			if (!line.startsWith("/m/"))
				continue;

			String[] parts = line.split("\t");

			if (parts.length < 3)
				continue;

			if (currentMid == null) {
				currentMid = parts[0];
			}


			if (!parts[0].equals(currentMid)) {
				counter++;
				properties.put("mid", currentMid);
				if (FILM_SCORES.get(currentMid) == null) {
					System.err.println("What is happening to mid : " + currentMid);
					System.exit(1);
				}
				float weight = ((float)FILM_SCORES.get(currentMid))/totalScores;
				String weightStr = new DecimalFormat("#.#########").format(weight);
				properties.put("weight", weightStr);
				Long nodeId = db.createNode(properties);
				MID2ID_MAP.put(currentMid, nodeId);

				properties.clear();
			}

			if (!parts[1].startsWith("/m/") && !parts[2].startsWith("/m/") && !(parts.length == 4 && parts[3].startsWith("/m/"))) {
				String rel = parts[1].replace("/", "_").substring(1);

				String currentStuff = (String) properties.get(rel);
				if (parts.length == 4) {

					if (currentStuff != null) {
						parts[3] = parts[3] + "|" + currentStuff;
					}
					properties.put(rel, parts[3]);

				} else if (parts.length == 3) {

					if (currentStuff != null) {
						parts[2] = parts[2] + "|" + currentStuff;
					}
					properties.put(rel, parts[2]);
				}
			}

			currentMid = parts[0];

			report.dots();
		}

		System.out.println("Counter : " + counter);

		report.finishImport("Nodes");
		System.out.println("Done notes importing");
	}

	void importRelationships(Reader reader) throws IOException {
		Map<String, Object> empty = new HashMap<String, Object>();
		BufferedReader bf = new BufferedReader(reader);
		long counter = 0;
		final RelType relType = new RelType();
		String line;
		report.reset();
		while ((line = bf.readLine()) != null) {
			if (!line.startsWith("/m/"))
				continue;

			String[] parts = line.split("\t");
			if (parts[1].startsWith("/m/")) 
				continue; //ignore case

			if (parts.length < 3)
				continue;

			String startNodeId = parts[0];
			String targetMid = null;

			if (parts[2].startsWith("/m/")) {
				targetMid = parts[2];
			} else if (parts.length == 4 && parts[3].startsWith("/m/")) {
				targetMid = parts[3];
			}

			if (MID2ID_MAP.get(targetMid) == null || MID2ID_MAP.get(startNodeId) == null) 
				continue;      	

			relType.update(parts[1]);

			counter++;
			db.createRelationship(MID2ID_MAP.get(startNodeId), MID2ID_MAP.get(targetMid), relType, empty);

			report.dots();
		}

		System.out.println("Rel Counter : " + counter);
		report.finishImport("Relationships");
	}

	void importIndex(String indexName, BatchInserterIndex index, Reader reader) throws IOException {

		BufferedReader bf = new BufferedReader(reader);
		long counter = 0;
		String line;
		report.reset();
		while ((line = bf.readLine()) != null) {   
			if (!line.startsWith("/m/"))
				continue;

			String[] parts = line.split("\t");

			if (parts.length < 3)
				continue;

			String startNodeId = parts[0];
			if (parts[1].equals("/type/object/name")) {
				counter++;
				final Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("mid", startNodeId);
				if (parts.length == 3)
					properties.put("name", parts[2]);
				else if (parts.length == 4)
					properties.put("name", parts[3]);

				Long id = MID2ID_MAP.get(startNodeId);
				if (id != null)
					index.add(id, properties);
			}

			report.dots();
		}

		System.out.println("Index Counter : " + counter);
		report.finishImport("Done inserting into " + indexName + " Index");
	}

	void importIndexName(String indexName, BatchInserterIndex index, Reader reader) throws IOException {

		BufferedReader bf = new BufferedReader(reader);
		long counter = 0;
		String line;
		report.reset();
		final Map<String, Object> properties = new HashMap<String, Object>();
		String currentNodeId = null;
		while ((line = bf.readLine()) != null) {   
			if (!line.startsWith("/m/"))
				continue;
			String[] parts = line.split("\t");

			if (parts.length < 3)
				continue;

			String startNodeId = parts[0];
			if (currentNodeId == null) {
				currentNodeId = startNodeId;
			}

			if (startNodeId.equals(currentNodeId)) {
				if (parts[1].equals("/type/object/name") ||
						parts[1].equals("/common/topic/description")) {
					String name = parts[1].substring(1).replace("/", "_");

					if (parts.length == 3)
						properties.put(name, parts[2].toLowerCase());
					else if (parts.length == 4)
						properties.put(name, parts[3].toLowerCase());

				} /* else {
                	String name = parts[1].substring(1).replace("/", "_");
                	if (parts.length == 3)
            			properties.put(name, parts[2].toLowerCase());
            		else if (parts.length == 4)
            			properties.put(name, parts[3].toLowerCase());
                } */

			} else {
				Long id = MID2ID_MAP.get(currentNodeId);
				if (id != null) {
					counter++;
					index.add(id, properties);  
				}
				properties.clear();
			}

			currentNodeId = startNodeId;

			report.dots();
		}

		System.out.println(indexName + " Index Counter : " + counter);
		report.finishImport("Done inserting into " + indexName + " Index");
	}


	void importIndexMid(String indexName, BatchInserterIndex index, Reader reader) throws IOException {

		BufferedReader bf = new BufferedReader(reader);
		long counter = 0;
		String line;
		report.reset();
		while ((line = bf.readLine()) != null) {   
			if (!line.startsWith("/m/"))
				continue;

			counter++;

			String[] parts = line.split("\t");

			if (parts.length < 3)
				continue;

			String startNodeId = parts[0];

			//if (parts[1].equals("/type/object/name")) {
			Long id = MID2ID_MAP.get(startNodeId);
			if (id != null) {
				final Map<String, Object> properties = new HashMap<String, Object>();
				properties.put("mid", startNodeId);
				index.add(id, properties);
				MID2ID_MAP.remove(startNodeId);
			}   

			report.dots();
		}


		System.out.println(indexName + " Index Counter : " + counter);
		report.finishImport("Done inserting into " + indexName + " Index");
	}



	private BatchInserterIndex nodeIndexFor(String indexName, String indexType) {
		return lucene.nodeIndex(indexName, configFor(indexType));
	}

	private BatchInserterIndex relationshipIndexFor(String indexName, String indexType) {
		return lucene.relationshipIndex(indexName, configFor(indexType));
	}

	private Map<String, String> configFor(String indexType) {
		return indexType.equals("fulltext") ? FULLTEXT_CONFIG : EXACT_CONFIG;
	}

	static class RelType implements RelationshipType {
		String name;

		public RelType update(Object value) {
			this.name = value.toString();
			return this;
		}

		public String name() {
			return name;
		}
	}


}