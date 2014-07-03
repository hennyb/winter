public class MainTest {

	// Simple query
	public static String query1 = "SELECT ?x\n" + 
		"WHERE {\n" + 
		"?x <http://example.org/test> <http://example.org/name>\n" +
		"}";
	
	// Query with two variables
	public static String query2 = "SELECT ?x ?y\n" + 
		"WHERE {\n" +
		"?x <http://example.org/test> ?y ." +
		"?y <http://example.org/title> <http://example.org/name>\n" + 
		"}";
	
	// Query with PREFIX and two variables
	public static String query3 = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" + 
	"SELECT ?name ?mbox\n" +
	"WHERE {\n" +
	"?x foaf:name ?name .\n" + 
	"?x foaf:mbox ?mbox\n" +
	"}";

	// Query with literal with arbitrary datatypes
	public static String query4 = "SELECT ?v\n" +
			"WHERE {\n" +
			"?v ?p \"abc\"^^<http://example.org/datatype#specialDatatype>\n" +
			"}";
	
	// Query with two prefixes and CONSTRUCT
	public static String query5 = "";
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
