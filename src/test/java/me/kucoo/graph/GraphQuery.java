package me.kucoo.graph;


import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;

public class GraphQuery
{
    private static final String DB_PATH = "/Users/mdo/workspace/Neo4jCypher/resources/datasets/social-2.db";
    String resultString;
    String columnsString;
    String nodeResult;
    String rows = "";

    public static void main( String[] args )
    {
    	GraphQuery javaQuery = new GraphQuery();
        javaQuery.run();
        
    }

    void run()
    {
        // START SNIPPET: execute
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        /*
        // add some data first
        Transaction tx = db.beginTx();
        try
        {
            Node refNode = db.getReferenceNode();
            refNode.setProperty( "name", "reference node" );
            tx.success();
        }
        finally
        {
            tx.finish();
        }
        */
        
        // let's execute a query now
        ExecutionEngine engine = new ExecutionEngine( db );
        ExecutionResult result = engine.execute( "start n=node(1) match (n) - [r] -> (p) return p,r " );
        System.out.println( result );
        // END SNIPPET: execute
        // START SNIPPET: columns
        List<String> columns = result.columns();
        System.out.println( columns );
        // END SNIPPET: columns
        // START SNIPPET: items
        Iterator<Node> n_column = result.columnAs( "p" );
        for ( Node node : IteratorUtil.asIterable( n_column ) )
        {
            // note: we're grabbing the name property from the node,
            // not from the n.name in this case.
            nodeResult = node + ": " + node.getProperty( "name" );
            System.out.println( nodeResult );
        }
        // END SNIPPET: items
        // the result is now empty, get a new one
        result = engine.execute( "start n=node(1) return n, n.name" );
        // START SNIPPET: rows
        for ( Map<String, Object> row : result )
        {
            for ( Entry<String, Object> column : row.entrySet() )
            {
                rows += column.getKey() + ": " + column.getValue() + "; ";
            }
            rows += "\n";
        }
        System.out.println("Here 1 : " +  rows );
        // END SNIPPET: rows
        resultString = result.toString();
        columnsString = columns.toString();
        db.shutdown();
    }
}