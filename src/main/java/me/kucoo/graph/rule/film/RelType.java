package me.kucoo.graph.rule.film;

import org.neo4j.graphdb.RelationshipType;

public class RelType implements RelationshipType {
	String name;

	public RelType(String name) {
		this.name = name;
	}
	
	public RelType update(Object value) {
		this.name = value.toString();
		return this;
	}

	public String name() {
		return name;
	}
}